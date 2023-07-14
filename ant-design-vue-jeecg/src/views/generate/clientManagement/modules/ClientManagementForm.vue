<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-model-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="clientName">
              <a-input v-model="model.clientName" placeholder="请输入客户名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="所在城市" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="city">
		      <j-area-linkage type="cascader" v-model="model.city" placeholder="请输入省市区" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="银行账号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bankAccount">
              <a-input v-model="model.bankAccount" placeholder="请输入银行账号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开户银行" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bankName">
              <a-input v-model="model.bankName" placeholder="请输入开户银行" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开户名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="accountName">
              <a-input v-model="model.accountName" placeholder="请输入开户名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开票地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kpAddress">
              <a-input v-model="model.kpAddress" placeholder="请输入开票地址" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="税号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taxId">
              <a-input v-model="model.taxId" placeholder="请输入税号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开票电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kpPhone">
              <a-input v-model="model.kpPhone" placeholder="请输入开票电话" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="开票要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kpRemark">
              <a-textarea v-model="model.kpRemark" rows="4" placeholder="请输入开票要求" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="收票人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="receiver">
              <a-input v-model="model.receiver" placeholder="请输入收票人" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="收票电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recvPhone">
              <a-input v-model="model.recvPhone" placeholder="请输入收票电话" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="收票地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recvAddress">
              <a-input v-model="model.recvAddress" placeholder="请输入收票地址" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-model-item label="邮编" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zipcode">
              <a-input-number v-model="model.zipcode" placeholder="请输入邮编" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="客户联系人" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="clientContactTable.loading"
          :columns="clientContactTable.columns"
          :dataSource="clientContactTable.dataSource"
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
    name: 'ClientManagementForm',
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
           clientName: [
              { required: true, message: '请输入客户名称!'},
           ],
        },
        refKeys: ['clientContact', ],
        tableKeys:['clientContact', ],
        activeKey: 'clientContact',
        // 客户联系人
        clientContactTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '姓名',
              key: 'name',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '性别',
              key: 'sex',
              type: JVXETypes.select,
              options:[],
              dictCode:"sex",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '生日',
              key: 'birthday',
              type: JVXETypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '职务',
              key: 'duty',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '联系电话',
              key: 'phone',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ pattern: "m", message: "${title}格式不正确" }],
            },
            {
              title: '邮件',
              key: 'email',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ pattern: "e", message: "${title}格式不正确" }],
            },
            {
              title: 'QQ',
              key: 'qq',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '工作地址',
              key: 'workAddress',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/clientManagement/clientManagement/add",
          edit: "/clientManagement/clientManagement/edit",
          queryById: "/clientManagement/clientManagement/queryById",
          clientContact: {
            list: '/clientManagement/clientManagement/queryClientContactByMainId'
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
        this.clientContactTable.dataSource=[]
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
          this.requestSubTableData(this.url.clientContact.list, params, this.clientContactTable)
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
          clientContactList: allValues.tablesValue[0].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>