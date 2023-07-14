<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="部门">
              <j-select-depart placeholder="请选择部门" v-model="queryParam.deptName"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目年限">
              <j-dict-select-tag placeholder="请选择项目年限" v-model="queryParam.projectYear" dictCode="project_year"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目编号">
                <a-input placeholder="请输入项目编号" v-model="queryParam.projectIndex"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目名称">
                <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目分类">
                <j-popup placeholder="请选择项目分类" v-model="queryParam.projectCategory" code="select_project_categories" org-fields="name" dest-fields="project_category" :field="getPopupField('project_category')" :multi="true"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="项目状态">
                <j-dict-select-tag placeholder="请选择项目状态" v-model="queryParam.projectStatus" dictCode="project_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="客户名称">
                <a-input placeholder="请输入客户名称" v-model="queryParam.clientName"></a-input>
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('项目管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" icon="download" @click="handleDownloadTemplate('项目管理')">下载模板</a-button>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
<!--        <a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
        <a style="margin-left: 24px" @click="$refs.table.toggleAllCheckboxRow()">清空</a>
      </div>
<!--      <a-table-->
<!--        ref="table"-->
<!--        size="middle"-->
<!--        bordered-->
<!--        rowKey="id"-->
<!--        class="j-table-force-nowrap"-->
<!--        :scroll="{x:true}"-->
<!--        :columns="columns"-->
<!--        :dataSource="dataSource"-->
<!--        :pagination="ipagination"-->
<!--        :loading="loading"-->
<!--        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"-->
<!--        @change="handleTableChange"-->
<!--        @expand="loadSubTable"-->
<!--      >-->
      <vxe-table
        border
        ref="table"
        size="medium"
        class="j-table-force-nowrap"
        max-height="550"
        :data=tableData1
        :expand-config="{accordion:true}"
        @checkbox-change="checkboxChange()"
        @checkbox-all="checkboxChange()"
        @change="handleTableChange"
      >
        <vxe-table-column type="checkbox" align="center" fixed="left" width="60"></vxe-table-column>
        <vxe-table-column field="indexId" align="center" title="序号" fixed="left" width=60></vxe-table-column>
        <vxe-table-column field="deptName_dictText" align="center" title="部门" width=150></vxe-table-column>
        <vxe-table-column field="projectYear_dictText" align="center" title="项目年限" width=100></vxe-table-column>
        <vxe-table-column field="projectIndex" align="center" title="项目编号" width="120" show-overflow></vxe-table-column>
        <vxe-table-column field="projectCategory" align="center" title="项目类别" width=150></vxe-table-column>
        <vxe-table-column field="projectName" align="center" title="项目名称" min-width=150></vxe-table-column>
        <vxe-table-column field="projectStatus_dictText" align="center" title="项目状态" width=100></vxe-table-column>
        <vxe-table-column field="clientName" align="center" title="客户" width=100></vxe-table-column>
        <vxe-table-column field="signDate" align="center" title="合同签订时间" :formatter="timeFormat" width=150></vxe-table-column>
        <vxe-table-column field="contractPeriod" align="center" title="合同周期" width=200></vxe-table-column>
        <vxe-table-column field="totalAmount" align="center" title="合同金额" :formatter="moneyFormatter" width=120></vxe-table-column>
        <vxe-table-column field="unpaidAmount" align="center" title="待收金额" :formatter="moneyFormatter" width=120></vxe-table-column>
        <vxe-table-column type="expand" align="center" title= "收款管理" width="80">
          <template v-slot:content="{row}">
            <a-tabs class="tab-container">
              <a-tab-pane key="1" tab="项目应收">
                <vxe-table border :data="row.projectPaymentTermVos">
                  <vxe-table-column field="spec" align="center" title="合同事项"></vxe-table-column>
                  <vxe-table-column field="paymentAmount" align="center" title="待收金额" :formatter="moneyFormatter"></vxe-table-column>
                  <vxe-table-column field="remark" align="center" title="备注" show-overflow></vxe-table-column>
                </vxe-table>
              </a-tab-pane>
              <a-tab-pane key="2" tab="项目实收">
                <vxe-table border :data="row.projectPaymentRecvVos">
                  <vxe-table-column field="kpDate" align="center" title="开票日期" :fomatter="timeFormat"></vxe-table-column>
                  <vxe-table-column field="paymentAmount" align="center" title="收款金额" :formatter="moneyFormatter"></vxe-table-column>
                  <vxe-table-column field="remark" align="center" title="备注"></vxe-table-column>
                </vxe-table>
              </a-tab-pane>
            </a-tabs>
          </template>
        </vxe-table-column>
        <vxe-table-column field="consultFee" align="center" title="专家费" :formatter="moneyFormatter" width="120"/>
        <vxe-table-column field="remark" align="center" title="备注" width="200" show-overflow/>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>
        <vxe-table-column align="center" title="操作" fixed="right" width="120">
          <template v-slot="{row}">
            <a @click="handleEdit(row)">编辑</a>
            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handleDetail(row)">详情</a>
                </a-menu-item>
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(row.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </template>
        </vxe-table-column>
      </vxe-table>

      <vxe-pager
        border
        :loading="loading1"
        :current-page="tablePage1.currentPage"
        :page-size="tablePage1.pageSize"
        :total="tablePage1.totalResult"
        :page-sizes="[10,20,30]"
        :layouts="['Total', 'PrevPage', 'JumpNumber', 'NextPage', 'Sizes', 'FullJump']"
        @page-change="handlePageChange1"/>
<!--      </a-table>-->
    </div>

    <project-management-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<style scoped>
  .tab-container {
    width: calc(100% - 200px);
    margin: 0 auto;
  }
</style>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ProjectManagementModal from './modules/ProjectManagementModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'
  import { AxiosInstance as axios } from 'axios'
  import { filterObj } from '../../../utils/util'
  import ProjectPaymentRecvSubTable from './subTables/ProjectPaymentRecvSubTable'
  import { getAction } from '../../../api/manage'
  import moment from 'moment'

  export default {
    name: "ProjectManagementList",
    mixins:[JeecgListMixin],
    components: {
      ProjectManagementModal
    },
    data () {
      return {
        description: '项目管理管理页面',
        // 表头
        columns: [
          // {
          //   title: '#',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title:'序号',
            align:"center",
            dataIndex: 'indexId'
          },
          {
            title:'部门',
            align:"center",
            dataIndex: 'deptName_dictText'
          },
          {
            title:'项目年限',
            align:"center",
            dataIndex: 'projectYear_dictText'
          },
          {
            title:'项目编号',
            align:"center",
            dataIndex: 'projectIndex'
          },
          {
            title:'项目类别',
            align:"center",
            dataIndex: 'projectCategory'
          },
          {
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'项目状态',
            align:"center",
            dataIndex: 'projectStatus_dictText'
          },
          {
            title:'客户',
            align:"center",
            dataIndex: 'clientName'
          },
          {
            title:'合同签订时间',
            align:"center",
            dataIndex: 'signDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'合同周期',
            align:"center",
            dataIndex: 'contractPeriod'
          },
          {
            title:'合同金额',
            align:"center",
            dataIndex: 'totalAmount'
          },
          {
            title:'待收金额',
            align:"center",
            dataIndex: 'unpaidAmount'
          },
          {
            title:'合同条款',
            children: [
              {title: '合同事项'},
              {title: '合同金额'},
              {title: '备注'}
            ],
            align:"center",
            dataIndex: 'projectPaymentTerm'
          },
          {
            title:'采购合同条款',
            children: [
              {
                title: '供应商',
                dataIndex: 'supplier'
              },
              {
                title: '事项',
                dataIndex: 'contractPaymentTermSpec'
              },
              {
                title: '金额',
                dataIndex: 'contractPaymentTermAmount'
              },
              {
                title: '备注',
                dataIndex: 'contractPaymentTermRemark'
              }
            ],
            align:"center",
            dataIndex: 'contractPaymentTerm'
          },
          {
            title:'收款',
            key:'paymentRecv',
            children: [
              {
                title: '收款金额',
                dataIndex: 'projectPaymentRecvAmount'
              },
              {
                title: '开票日期',
                dataIndex: 'projectPaymentKpDate'
              },
              {
                title: '收款日期',
                dataIndex: 'projectPaymentRecvDate'
              },
              {
                title: '备注',
                dataIndex: 'projectPaymentRemark'
              }
            ],
            align:"center",
            dataIndex: 'projectPaymentRecv'
          },
          {
            title:'付款',
            children: [
              {
                title: '付款金额',
                dataIndex: 'contractPaymentRecvAmount'
              },
              {
                title: '开票日期',
                dataIndex: 'contractPaymentKpDate'
              },
              {
                title: '付款日期',
                dataIndex: 'contractPaymentRecvDate'
              },
              {
                title: '备注',
                dataIndex: 'contractPaymentRemark'
              }
            ],
            align:"center",
            dataIndex: 'contractPaymentRecv'
          },
          {
            title:'专家费',
            align:"center",
            dataIndex: 'consultFee'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/projectManagement/projectManagement/list",
          delete: "/projectManagement/projectManagement/delete",
          deleteBatch: "/projectManagement/projectManagement/deleteBatch",
          exportXlsUrl: "/projectManagement/projectManagement/exportXls",
          downTemplate: "/projectManagement/projectManagement/downTemplate",
          importExcelUrl: "projectManagement/projectManagement/importExcel",
          queryProjectPaymentTerm: "projectManagement/projectManagement/queryProjectPaymentTermByMainId",
          queryProjectPaymentRecv: "projectManagement/projectManagement/queryProjectPaymentRecvByMainId",
        },
        dictOptions:{},
        superFieldList:[],
        subTableDataSource: [],
        subLoading: true,
        projectPaymentRecvColumns: [
          {
            title: '收款金额',
            dataIndex: 'paymentAmount'
          },
          {
            title: '开票日期',
            dataIndex: 'kpDate'
          },
          {
            title: '收款日期',
            dataIndex: 'payDate'
          },
          {
            title: '备注',
            dataIndex: 'remark'
          },
        ],
        projectPaymentTermColumns: [
          {
            title: '合同事项',
            dataIndex: 'spec'
          },
          {
            title: '合同金额',
            dataIndex: 'paymentAmount'
          },
          {
            title: '备注',
            dataIndex: 'remark'
          },
        ],
        refKeys: ['projectPaymentTerm', 'projectPaymentRecv',],
        activeKey: 'projectPaymentTerm',
        loading1:false,
        tableData1: [],
        tablePage1:{
          currentPage: 1,
          pageSize: 10,
          totalResult: 0
        },
      }
    },
    created() {
      this.getSuperFieldList()
      this.findList1()
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      timeFormat (row) {
        console.log('formatting sign date')
        return row.cellValue ? moment(row.cellValue).format('YYYY-MM-DD') : ''
      },
      findList1() {
        this.loading1 = true
        this.loadData(1)
        this.loading1 = false
      },
      handlePageChange1 ({ currentPage, pageSize }) {
        this.tablePage1.currentPage = currentPage
        this.tablePage1.pageSize = pageSize
        this.findList1()
      },
      getSuperFieldList(){
        let fieldList=[]
        fieldList.push({type:'int',value:'indexId',text:'序号',dictCode:''})
        fieldList.push({type:'sel_depart',value:'deptName',text:'部门'})
        fieldList.push({type:'string',value:'projectYear',text:'项目年限',dictCode:'project_year'})
        fieldList.push({type:'string',value:'projectIndex',text:'项目编号',dictCode:''})
        fieldList.push({type:'string',value:'projectName',text:'项目名称',dictCode:''})
        fieldList.push({type:'popup',value:'projectCategory',text:'项目分类', popup:{code:'select_project_categories',field:'name',orgFields:'name',destFields:'project_category'}})
        fieldList.push({type:'string',value:'projectStatus',text:'项目状态',dictCode:'project_status'})
        fieldList.push({type:'string',value:'clientName',text:'客户名称',dictCode:''})
        fieldList.push({type:'date',value:'signDate',text:'合同签订时间'})
        fieldList.push({type:'string',value:'contractPeriod',text:'合同周期',dictCode:''})
        fieldList.push({type:'string',value:'contractScanned',text:'合同扫描件',dictCode:''})
        fieldList.push({type:'list',value:'contractPaymentTerm',text:'合同应付',dictCode:''})
        fieldList.push({type:'list',value:'contractPaymentRecv',text:'合同实付',dictCode:''})
        fieldList.push({type:'double',value:'consultFee',text:'专家费',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>