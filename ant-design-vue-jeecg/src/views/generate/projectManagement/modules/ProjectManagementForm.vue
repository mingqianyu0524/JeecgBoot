<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deptName">
              <j-select-depart v-model="model.deptName" :multi="true"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目年限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectYear">
              <j-dict-select-tag type="list" v-model="model.projectYear" dictCode="project_year" placeholder="请选择项目年限" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectIndex">
              <a-input v-model="model.projectIndex" placeholder="请输入项目编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectName">
              <a-input v-model="model.projectName" placeholder="请输入项目名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目分类" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectCategory">
              <j-popup
                v-model="model.projectCategory"
                field="projectCategory"
                org-fields="name"
                dest-fields="projectCategory"
                code="select_project_categories"
                :multi="true"
                @input="popupCallback"
                />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="项目状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectStatus">
              <j-dict-select-tag type="list" v-model="model.projectStatus" dictCode="project_status" placeholder="请选择项目状态" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clientName">
              <j-dict-select-tag type="list" v-model="model.clientName" dictCode="client_management where is_delete = 0,client_name,client_name" placeholder="请选择客户名称" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同签订时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signDate">
              <j-date placeholder="请选择合同签订时间" v-model="model.signDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同周期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请输入合同开始时间" v-model="model.contractStartDate" style="width: 50%" />
              <j-date placeholder="请输入合同结束时间" v-model="model.contractEndDate" style="width: 50%" />
<!--              <a-range-picker v-model:value="model.contractPeriod"/>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同扫描件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractScanned">
              <j-image-upload isMultiple v-model="model.contractScannedString" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="专家费" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="consultFee">
              <a-input-number v-model="model.consultFee" placeholder="请输入专家费" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-textarea v-model="model.remark" rows="4" placeholder="请输入备注" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
<!--      <a-tab-pane tab="合同详情" :key="refKeys[0]" :forceRender="true">-->
<!--        <div v-for="data in contractTable.dataSource">-->
<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectIndex">-->
<!--                <a-input v-model="data.contractIndex" placeholder="请输入合同编号" ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectIndex">-->
<!--                <a-input v-model="data.contractName" placeholder="请输入合同名称" ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-model-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectIndex">-->
<!--                <a-input v-model="data.supplierName" placeholder="请输入供应商名称" ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
<!--          </a-row>-->
<!--          <h3>合同应付</h3>-->
<!--          <vxe-table :data=data.contractPaymentTermList>-->
<!--            <vxe-table-column field="spec" align="center" title="说明"/>-->
<!--            <vxe-table-column field="paymentAmount" align="center" title="应付金额"/>-->
<!--            <vxe-table-column field="remark" align="center" title="备注"/>-->
<!--          </vxe-table>-->
<!--          <span> 合计：{{ sumAmount(data.contractPaymentTermList).toFixed(2) }} </span>-->
<!--          <br/> <br/>-->
<!--          <h3>合同实付</h3>-->
<!--          <vxe-table :data="data.contractPaymentRecvList">-->
<!--            <vxe-table-column field="paymentAmount" align="center" title="实付金额"/>-->
<!--            <vxe-table-column field="kpDate" align="center" title="开票日期"/>-->
<!--            <vxe-table-column field="payDate" align="center" title="付款日期"/>-->
<!--            <vxe-table-column field="remark" align="center" title="备注"/>-->
<!--          </vxe-table>-->
<!--          <span> 合计：{{ sumAmount(data.contractPaymentRecvList).toFixed(2) }} </span>-->
<!--          <br/>-->
<!--          <a-divider />-->
<!--        </div>-->
<!--      </a-tab-pane>-->
      <a-tab-pane tab="项目应收" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="projectPaymentTermTable.loading"
          :columns="projectPaymentTermTable.columns"
          :dataSource="projectPaymentTermTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
        <span v-if="!projectPaymentTermTable.loading"> 合计：{{ paymentTermSum }} </span>
      </a-tab-pane>
      <a-tab-pane tab="收款管理" :key="refKeys[1]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[1]"
          :loading="projectPaymentRecvTable.loading"
          :columns="projectPaymentRecvTable.columns"
          :dataSource="projectPaymentRecvTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
        <span v-if="!projectPaymentRecvTable.loading"> 合计：{{ paymentRecvSum }} </span>
      </a-tab-pane>
      <a-tab-pane tab="操作日志" :key="refKeys[2]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[2]"
          :loading="operationLog.loading"
          :columns="operationLog.columns"
          :dataSource="operationLog.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="false"
          :toolbar="false">
<!--          <vxe-table-column title="图片" width="140" align="center">-->
<!--            <template #default>-->
<!--            <img src="https://static.jeecg.com/temp/jmlogo_1606575041993.png" style="width: 100px;">-->
<!--          </template>-->
<!--          </vxe-table-column>-->
        </j-vxe-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import { getRefPromise,VALIDATE_FAILED} from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  // import { ref } from '@vue/composition-api'

  // 项目应收
  let projectPaymentTermTable = {
    loading: false,
    dataSource: [],
    columns: [
      {
        title: '说明',
        key: 'spec',
        type: JVXETypes.input,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '金额',
        key: 'paymentAmount',
        type: JVXETypes.input,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '备注',
        key: 'remark',
        type: JVXETypes.input,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
    ]
  }
  // 操作日志
  let operationLog = {
    loading: false,
      dataSource: [],
      columns: [
      {
        title: '用户名',
        key: 'username',
        type: 'string',
        width:"200px",
      },
      {
        title: '操作内容',
        key: 'logContent',
        type: 'string',
        width:"200px",
      },
      {
        title: '操作时间',
        key: 'createTime',
        type: 'string',
        width:"200px",
      },
    ]
  }
  // 收款管理
  let projectPaymentRecvTable = {
    loading: false,
      dataSource: [],
      columns: [
      {
        title: '收款流水号',
        key: 'paymentSerialNumber',
        type: JVXETypes.input,
        disabled:true,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '收款金额',
        key: 'paymentAmount',
        type: JVXETypes.input,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
        validateRules: [{ required: true, message: '${title}不能为空' }],
      },
      {
        title: '开票日期',
        key: 'kpDate',
        type: JVXETypes.date,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '到款日期',
        key: 'recvDate',
        type: JVXETypes.date,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '备注',
        key: 'remark',
        type: JVXETypes.textarea,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },

      {
        title: 'project_fk_id',
        key: 'project_fk_id',
        type:"hidden"
      },

      {
        title: 'project_index',
        key: 'project_index',
        type:"hidden"
      },

      {
        title: 'project_name',
        key: 'project_name',
        type:"hidden"
      },

      {
        title: 'client_name',
        key: 'client_name',
        type:"hidden"
      },
    ]
  }
  // 合同管理
  let contractTable = {
    loading: false,
    dataSource: [],
    columns: [
      {
        title: '说明',
        key: 'spec',
        type: JVXETypes.input,
        disabled: true,
        width:"200px",
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '金额',
        key: 'paymentAmount',
        type: JVXETypes.input,
        width:"200px",
        disabled: true,
        placeholder: '请输入${title}',
        defaultValue:'',
      },
      {
        title: '备注',
        key: 'remark',
        type: JVXETypes.input,
        width:"200px",
        disabled: true,
        placeholder: '请输入${title}',
        defaultValue:'',
      },
    ]
  }

  export default {
    name: 'ProjectManagementForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // value1: null,
        model:{

         },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
           projectYear: [
              { required: true, message: '请输入项目年限!'},
           ],
           projectIndex: [
              { required: true, message: '请输入项目编号!'},
           ],
           projectName: [
              { required: true, message: '请输入项目名称!'},
           ],
           projectCategory: [
              { required: true, message: '请输入项目分类!'},
           ],
           projectStatus: [
              { required: true, message: '请输入项目状态!'},
           ],
           clientName: [
              { required: true, message: '请输入客户名称!'},
           ],
        },
        // refKeys: ['contractTable', 'projectPaymentTerm', 'projectPaymentRecv', 'operationLog', ],
        // tableKeys:['contractTable', 'projectPaymentTerm', 'projectPaymentRecv', 'operationLog', ],
        refKeys: ['projectPaymentTerm', 'projectPaymentRecv', 'operationLog', ],
        tableKeys:['projectPaymentTerm', 'projectPaymentRecv', 'operationLog', ],
        activeKey: 'projectPaymentTerm',
        projectPaymentTermTable: projectPaymentTermTable,
        projectPaymentRecvTable: projectPaymentRecvTable,
        operationLog: operationLog,
        contractTable: contractTable,
        url: {
          add: "/projectManagement/projectManagement/add",
          edit: "/projectManagement/projectManagement/edit",
          queryById: "/projectManagement/projectManagement/queryById",
          projectPaymentTerm: {
            list: '/projectManagement/projectManagement/queryProjectPaymentTermByMainId'
          },
          projectPaymentRecv: {
            list: '/projectManagement/projectManagement/queryProjectPaymentRecvByMainId'
          },
          operationLog: {
            list: '/projectManagement/projectManagement/queryOperationLog'
          },
          contractPaymentTerm: {
            listByProjectId: '/contractManagement/contractManagement/listByProjectId'
          },
        },
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled() {
        return this.disabled
      },
      paymentTermSum() {
        return projectPaymentTermTable.dataSource.reduce((acc, cur) => acc + cur.paymentAmount, 0)
      },
      paymentRecvSum() {
        return projectPaymentRecvTable.dataSource.reduce((acc, cur) => acc + cur.paymentAmount, 0)
      },
    },
    created () {
      // this.value1 = ref()
    },
    watch: {
      projectPaymentTermTable: {
        handler(newVal) {
          console.log('project payment term table updated')
          this.$nextTick(() => {
            this.$forceUpdate()
          })
        },
        deep: true,
      }
    },
    methods: {
      sumAmount(data) {
        return data
          .map(row => row.paymentAmount)
          .reduce((acc,cur) => parseFloat(cur) + acc, 0)
      },
      addBefore(){
        this.projectPaymentTermTable.dataSource=[]
        this.projectPaymentRecvTable.dataSource=[]
        this.operationLog.dataSource = []
        this.contractTable.dataSource = []
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          console.log('debug edit', params)
          // let params_ = { projectFkId: this.model.id }
          this.requestSubTableData(this.url.projectPaymentTerm.list, params, this.projectPaymentTermTable)
          this.requestSubTableData(this.url.projectPaymentRecv.list, params, this.projectPaymentRecvTable)
          this.requestSubTableData(this.url.operationLog.list, params, this.operationLog)
          // this.requestSubTableData(this.url.contractPaymentTerm.listByProjectId, params_, this.contractTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
              resolve(allValues)
            }).catch(e => {
              if (e.error === VALIDATE_FAILED) {
                // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
              } else {
                console.error(e)
              }
            })
          })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          // contractTable: allValues.tablesValue[0].tableData,
          projectPaymentTermList: allValues.tablesValue[0].tableData,
          projectPaymentRecvList: allValues.tablesValue[1].tableData,
          operationLog: allValues.tablesValue[2].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(value,row){
        this.model = Object.assign(this.model, row);
      },
    },
  }
</script>

<style scoped>
</style>