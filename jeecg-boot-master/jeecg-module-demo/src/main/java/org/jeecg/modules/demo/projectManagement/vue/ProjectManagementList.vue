<template>
  <a-card :bordered="false">
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
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <project-management-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ProjectManagementModal from './modules/ProjectManagementModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'

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
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
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
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName'
          },
          {
            title:'项目分类',
            align:"center",
            dataIndex: 'projectCategory'
          },
          {
            title:'项目状态',
            align:"center",
            dataIndex: 'projectStatus_dictText'
          },
          {
            title:'客户名称',
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
            title:'合同应付',
            align:"center",
            dataIndex: 'contractPaymentTerm'
          },
          {
            title:'合同实付',
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
          importExcelUrl: "projectManagement/projectManagement/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
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
        fieldList.push({type:'string',value:'contractPaymentTerm',text:'合同应付',dictCode:''})
        fieldList.push({type:'string',value:'contractPaymentRecv',text:'合同实付',dictCode:''})
        fieldList.push({type:'double',value:'consultFee',text:'专家费',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>