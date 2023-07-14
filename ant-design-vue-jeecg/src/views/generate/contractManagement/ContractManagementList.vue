<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目编号">
              <j-popup placeholder="请选择项目编号" v-model="queryParam.projectFkId" code="select_all_projects" org-fields="id,project_index,client_name" dest-fields="project_fk_id,project_index,client_name" :field="getPopupField('project_fk_id')" :multi="true"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="合同名称">
              <a-input placeholder="请输入合同名称" v-model="queryParam.contractName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="合同签订时间">
                <j-date placeholder="请选择合同签订时间" v-model="queryParam.contractSignDate"></j-date>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="供应商名称">
                <j-popup placeholder="请选择供应商名称" v-model="queryParam.supplierName" code="select_supplier" org-fields="supplier_name" dest-fields="supplier_name" :field="getPopupField('supplier_name')" :multi="true"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('合同管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" icon="download" @click="handleDownloadTemplate('合同管理')">下载模板</a-button>
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
<!--        @change="handleTableChange">-->
<!--        <template #expandedRowRender>-->
<!--          <a-tabs v-model:activeKey="activeKey" type="card">-->
<!--            <a-tab-pane tab="tab1">-->
<!--              <a-table-->
<!--                ref="subTable"-->
<!--                :columns="subColumns"-->
<!--                :dataSource="subTableData"-->
<!--                :loading="loading"-->
<!--              />-->
<!--            </a-tab-pane>-->
<!--            <a-tab-pane tab="2"/>-->
<!--          </a-tabs>-->
<!--        </template>-->

      <vxe-table
        border
        ref="table"
        size="medium"
        class="j-table-force-nowrap"
        max-height="550"
        :data="tableData1"
        :expand-config="{accordion:true}"
        @checkbox-change="checkboxChange()"
        @checkbox-all="checkboxChange()"
        @change="handleTableChange"
      >
        <vxe-table-column type="checkbox" align="center" fixed="left" width="60"></vxe-table-column>
        <vxe-table-column field="indexId" align="center" title="序号" fixed="left" width=60></vxe-table-column>
        <vxe-table-column field="deptName_dictText" align="center" title="部门" width=150></vxe-table-column>
        <vxe-table-column field="projectIndex" align="center" title="项目编号" width="150"></vxe-table-column>
        <vxe-table-column field="contractName" align="center" title="合同名称" min-width=150></vxe-table-column>
        <vxe-table-column field="contractSignDate" align="center" title="合同签订时间" :formatter="timeFormat" width=150></vxe-table-column>
        <vxe-table-column field="contractPeriod" align="center" title="合同周期" width=200></vxe-table-column>
        <vxe-table-column field="totalAmount" align="center" title="合同金额" :formatter="moneyFormatter" width=120></vxe-table-column>
        <vxe-table-column field="unpaidAmount" align="center" title="待付金额" :formatter="moneyFormatter" width=120></vxe-table-column>
        <vxe-table-column type="expand" align="center" title= "付款管理" width="80">
          <template v-slot:content="{row}">
            <a-tabs class="tab-container">
              <a-tab-pane key="1" tab="合同应收">
                <vxe-table border :data="row.contractPaymentTermVos">
                  <vxe-table-column field="spec" align="center" title="合同事项"></vxe-table-column>
                  <vxe-table-column field="paymentAmount" align="center" title="待付金额":formatter="moneyFormatter"></vxe-table-column>
                  <vxe-table-column field="remark" align="center" title="备注" show-overflow></vxe-table-column>
                </vxe-table>
              </a-tab-pane>
              <a-tab-pane key="2" tab="合同实收">
                <vxe-table border :data="row.contractPaymentRecvVos">
                  <vxe-table-column field="kpDate" align="center" title="开票日期" :fomatter="timeFormat"></vxe-table-column>
                  <vxe-table-column field="paymentAmount" align="center" title="付款金额" :formatter="moneyFormatter"></vxe-table-column>
                  <vxe-table-column field="remark" align="center" title="备注"></vxe-table-column>
                </vxe-table>
              </a-tab-pane>
            </a-tabs>
          </template>
        </vxe-table-column>
        <vxe-table-column field="remark" align="center" title="备注" width=200 show-overflow/>

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

    <contract-management-modal ref="modalForm" @ok="modalFormOk"/>
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
  import ContractManagementModal from './modules/ContractManagementModal'
  import '@/assets/less/TableExpand.less'
  import moment from 'moment'
  import { getAction } from '../../../api/manage'

  export default {
    name: "ContractManagementList",
    mixins:[JeecgListMixin],
    components: {
      ContractManagementModal
    },
    data () {
      return {
        description: '合同管理管理页面',
        model: {},
        // 表头
        subColumns: [
          {
            title:'合同事项',
            align:"center",
            dataIndex:'spec'
          },
          {
            title:'合同金额',
            align:"center",
            dataIndex:'paymentAmount'
          },
          {
            title:'备注',
            align:"center",
            dataIndex:'remark'
          },
        ],
        subTableData: [],
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
            title:'对应项目编号',
            align:"center",
            dataIndex: 'projectIndex'
          },
          {
            title:'合同名称',
            align:"center",
            dataIndex: 'contractName'
          },
          {
            title:'合同签订时间',
            align:"center",
            dataIndex: 'contractSignDate',
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
            title:'待付金额',
            align:"center",
            dataIndex: 'unpaidAmount'
          },
          {
            title:'合同条款',
            align:"center",
            dataIndex: 'contractPaymentTermVo'
          },
          {
            title:'付款',
            align:"center",
            dataIndex: 'contractPaymentRecvVo'
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
          list: "/contractManagement/contractManagement/list",
          delete: "/contractManagement/contractManagement/delete",
          deleteBatch: "/contractManagement/contractManagement/deleteBatch",
          exportXlsUrl: "/contractManagement/contractManagement/exportXls",
          downTemplate: "/contractManagement/contractManagement/downTemplate",
          importExcelUrl: "contractManagement/contractManagement/importExcel",
          queryContractPaymentRecvByMainId: "contractManagement/contractManagement/queryContractPaymentRecvByMainId",
        },
        dictOptions:{},
        superFieldList:[],
        activeKey: 'contractPaymentTerm',
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
        let fieldList=[];
        fieldList.push({type:'int',value:'indexId',text:'序号',dictCode:''})
        fieldList.push({type:'sel_depart',value:'deptName',text:'部门'})
        fieldList.push({type:'popup',value:'projectIndex',text:'项目编号', popup:{code:'select_all_projects',field:'id',orgFields:'id',destFields:'project_fk_id'}})
        fieldList.push({type:'string',value:'contractIndex',text:'合同编号',dictCode:''})
        fieldList.push({type:'string',value:'contractName',text:'合同名称',dictCode:''})
        fieldList.push({type:'date',value:'contractSignDate',text:'合同签订时间'})
        fieldList.push({type:'string',value:'contractPeriod',text:'合同周期',dictCode:''})
        fieldList.push({type:'string',value:'clientName',text:'客户名称',dictCode:''})
        fieldList.push({type:'popup',value:'supplierName',text:'供应商名称', popup:{code:'select_supplier',field:'supplier_name',orgFields:'supplier_name',destFields:'supplier_name'}})
        fieldList.push({type:'string',value:'contractScanned',text:'合同扫描件',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>