<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目编号">
              <j-popup placeholder="请选择项目编号" v-model="queryParam.projectFkId" code="select_all_projects" org-fields="id,project_index,project_name,client_name" dest-fields="project_fk_id,project_index,project_name,client_name" :field="getPopupField('project_fk_id,project_index,project_name,client_name')" :multi="true"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="项目名称">
              <a-input placeholder="请输入项目名称" v-model="queryParam.projectName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
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
      <a-button type="primary" icon="download" @click="handleExportXls('收款管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-button type="primary" icon="download" @click="handleDownloadTemplate('收款管理')">下载模板</a-button>
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
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

    <project-payment-recv_-modal ref="modalForm" @ok="modalFormOk"></project-payment-recv_-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ProjectPaymentRecv_Modal from './modules/ProjectPaymentRecv_Modal'

  export default {
    name: 'ProjectPaymentRecv_List',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ProjectPaymentRecv_Modal
    },
    data () {
      return {
        description: '收款管理管理页面',
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
            title:'收款流水号',
            align:"center",
            dataIndex: 'paymentSerialNumber'
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
            title:'客户名称',
            align:"center",
            dataIndex: 'clientName'
          },
          {
            title:'收款金额',
            align:"center",
            dataIndex: 'paymentAmount',
            customRender: (text) => {
              if (!text) return '0.00'
              // 将数据分割，保留两位小数
              text= text.toFixed(2)
              // 获取整数部分
              const intPart = Math.trunc(text)
              // 整数部分处理，增加,
              const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
              // 预定义小数部分
              let floatPart = '.00'
              // 将数据分割为小数部分和整数部分
              const newArr = text.toString().split('.')
              if (newArr.length === 2) { // 有小数部分
                floatPart = newArr[1].toString() // 取得小数部分
                return '￥' + intPartFormat + '.' + floatPart
              }
              return '￥' + intPartFormat + floatPart
            }
          },
          {
            title:'开票日期',
            align:"center",
            dataIndex: 'kpDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'到款日期',
            align:"center",
            dataIndex: 'recvDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
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
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/projectPaymentRecv/projectPaymentRecv_/list",
          delete: "/projectPaymentRecv/projectPaymentRecv_/delete",
          deleteBatch: "/projectPaymentRecv/projectPaymentRecv_/deleteBatch",
          exportXlsUrl: "/projectPaymentRecv/projectPaymentRecv_/exportXls",
          downTemplate: "/projectPaymentRecv/projectPaymentRecv_/downTemplate",
          importExcelUrl: "projectPaymentRecv/projectPaymentRecv_/importExcel",

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
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'paymentSerialNumber',text:'收款流水号',dictCode:''})
        fieldList.push({type:'popup',value:'projectIndex',text:'项目编号', popup:{code:'select_all_projects',field:'id',orgFields:'id',destFields:'project_fk_id'}})
        fieldList.push({type:'string',value:'projectName',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'clientName',text:'客户名称',dictCode:''})
        fieldList.push({type:'double',value:'paymentAmount',text:'收款金额',dictCode:''})
        fieldList.push({type:'date',value:'kpDate',text:'开票日期'})
        fieldList.push({type:'date',value:'recvDate',text:'到款日期'})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>