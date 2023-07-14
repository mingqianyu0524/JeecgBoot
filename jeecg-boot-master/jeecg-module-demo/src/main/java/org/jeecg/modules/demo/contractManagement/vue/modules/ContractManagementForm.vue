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
            <a-form-model-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectIndex">
              <j-popup
                v-model="model.projectIndex"
                field="projectIndex"
                org-fields="id,project_index,client_name"
                dest-fields="projectFkId,projectIndex,clientName"
                code="select_all_projects"
                :multi="true"
                @input="popupCallback"
                />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractIndex">
              <a-input v-model="model.contractIndex" placeholder="请输入合同编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractName">
              <a-input v-model="model.contractName" placeholder="请输入合同名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同签订时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractSignDate">
              <j-date placeholder="请选择合同签订时间" v-model="model.contractSignDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同周期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractPeriod">
              <a-input v-model="model.contractPeriod" placeholder="请输入合同周期" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clientName">
              <a-input v-model="model.clientName" placeholder="请输入客户名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplierName">
              <j-popup
                v-model="model.supplierName"
                field="supplierName"
                org-fields="supplier_name"
                dest-fields="supplierName"
                code="select_supplier"
                :multi="true"
                @input="popupCallback"
                />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同扫描件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractScanned">
              <j-image-upload isMultiple  v-model="model.contractScanned" ></j-image-upload>
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
      <a-tab-pane tab="合同应付" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="contractPaymentTermTable.loading"
          :columns="contractPaymentTermTable.columns"
          :dataSource="contractPaymentTermTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
      </a-tab-pane>
      <a-tab-pane tab="付款管理" :key="refKeys[1]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[1]"
          :loading="contractPaymentRecvTable.loading"
          :columns="contractPaymentRecvTable.columns"
          :dataSource="contractPaymentRecvTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
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

  export default {
    name: 'ContractManagementForm',
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
        model:{
         },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
           deptName: [
              { required: true, message: '请输入部门!'},
           ],
           projectIndex: [
              { required: true, message: '请输入项目编号!'},
           ],
           contractIndex: [
              { required: true, message: '请输入合同编号!'},
           ],
        },
        refKeys: ['contractPaymentTerm', 'contractPaymentRecv', ],
        tableKeys:['contractPaymentTerm', 'contractPaymentRecv', ],
        activeKey: 'contractPaymentTerm',
        // 合同应付
        contractPaymentTermTable: {
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
        },
        // 付款管理
        contractPaymentRecvTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '付款流水号',
              key: 'paymentSerialNumber',
               type: JVXETypes.input,
               disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '合同编号',
              key: 'contractIndex',
              type: JVXETypes.popup,
              popupCode:"select_all_contracts",
              field:"contract_index,contract_name,id,project_index,project_name",
              orgFields:"contract_index,contract_name,id,project_index,project_name",
              destFields:"contractIndex,contractName,contractFkId,projectIndex,projectName",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '项目编号',
              key: 'projectIndex',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '项目名称',
              key: 'projectName',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '付款金额',
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
              title: '付款日期',
              key: 'payDate',
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
              title: 'contract_index',
              key: 'contract_index',
              type:"hidden"
            },

            {
              title: 'contract_name',
              key: 'contract_name',
              type:"hidden"
            },

            {
              title: 'contract_fk_id',
              key: 'contract_fk_id',
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
          ]
        },
        url: {
          add: "/contractManagement/contractManagement/add",
          edit: "/contractManagement/contractManagement/edit",
          queryById: "/contractManagement/contractManagement/queryById",
          contractPaymentTerm: {
            list: '/contractManagement/contractManagement/queryContractPaymentTermByMainId'
          },
          contractPaymentRecv: {
            list: '/contractManagement/contractManagement/queryContractPaymentRecvByMainId'
          },
        }
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
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.contractPaymentTermTable.dataSource=[]
        this.contractPaymentRecvTable.dataSource=[]
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
          this.requestSubTableData(this.url.contractPaymentTerm.list, params, this.contractPaymentTermTable)
          this.requestSubTableData(this.url.contractPaymentRecv.list, params, this.contractPaymentRecvTable)
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
          contractPaymentTermList: allValues.tablesValue[0].tableData,
          contractPaymentRecvList: allValues.tablesValue[1].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(value,row){
      this.model = Object.assign(this.model, row);
     },

    }
  }
</script>

<style scoped>
</style>