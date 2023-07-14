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
              <a-input v-model="model.clientName" placeholder="请输入客户名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同签订时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="signDate">
              <j-date placeholder="请选择合同签订时间" v-model="model.signDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同周期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractPeriod">
              <a-input v-model="model.contractPeriod" placeholder="请输入合同周期" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="合同扫描件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contractScanned">
              <j-image-upload isMultiple  v-model="model.contractScanned" ></j-image-upload>
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
        refKeys: ['projectPaymentTerm', 'projectPaymentRecv', ],
        tableKeys:['projectPaymentTerm', 'projectPaymentRecv', ],
        activeKey: 'projectPaymentTerm',
        // 项目应收
        projectPaymentTermTable: {
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
        // 收款管理
        projectPaymentRecvTable: {
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
              title: '项目编号',
              key: 'projectIndex',
              type: JVXETypes.popup,
              popupCode:"select_all_projects",
              field:"id,project_index,project_name,client_name",
              orgFields:"id,project_index,project_name,client_name",
              destFields:"projectFkId,projectIndex,projectName,clientName",
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
              title: '客户名称',
              key: 'clientName',
               type: JVXETypes.input,
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
        },
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
        this.projectPaymentTermTable.dataSource=[]
        this.projectPaymentRecvTable.dataSource=[]
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
          this.requestSubTableData(this.url.projectPaymentTerm.list, params, this.projectPaymentTermTable)
          this.requestSubTableData(this.url.projectPaymentRecv.list, params, this.projectPaymentRecvTable)
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
          projectPaymentTermList: allValues.tablesValue[0].tableData,
          projectPaymentRecvList: allValues.tablesValue[1].tableData,
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