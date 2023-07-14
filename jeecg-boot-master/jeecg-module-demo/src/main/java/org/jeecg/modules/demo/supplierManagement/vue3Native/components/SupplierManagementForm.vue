<template>
  <a-spin :spinning="loading">
    <a-form v-bind="formItemLayout">
      <a-row>
        <a-col :span="24">
          <a-form-item label="供应商编号" v-bind="validateInfos.supplierIndex">
	          <a-input-number v-model:value="formData.supplierIndex" placeholder="请输入供应商编号" style="width: 100%" disabled/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="供应商名称" v-bind="validateInfos.supplierName">
            <a-input v-model:value="formData.supplierName" placeholder="请输入供应商名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="所在城市" v-bind="validateInfos.city">
	          <j-area-linkage v-model:value="formData.city" placeholder="请输入所在城市" :disabled="disabled" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="联系人" v-bind="validateInfos.contact">
	          <j-search-select v-model:value="formData.contact" dict="supplier_contact,name,name" :disabled="disabled" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="银行账号" v-bind="validateInfos.bankAccount">
            <a-input v-model:value="formData.bankAccount" placeholder="请输入银行账号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开户银行" v-bind="validateInfos.bankName">
            <a-input v-model:value="formData.bankName" placeholder="请输入开户银行" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开户名称" v-bind="validateInfos.accountName">
            <a-input v-model:value="formData.accountName" placeholder="请输入开户名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开票地址" v-bind="validateInfos.kpAddress">
            <a-input v-model:value="formData.kpAddress" placeholder="请输入开票地址" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="税号" v-bind="validateInfos.taxId">
            <a-input v-model:value="formData.taxId" placeholder="请输入税号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开票电话" v-bind="validateInfos.kpPhone">
            <a-input v-model:value="formData.kpPhone" placeholder="请输入开票电话" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="开票要求" v-bind="validateInfos.kpRemark">
	          <a-textarea v-model:value="formData.kpRemark" rows="4" placeholder="请输入开票要求" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="收票人" v-bind="validateInfos.receiver">
            <a-input v-model:value="formData.receiver" placeholder="请输入收票人" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="收票电话" v-bind="validateInfos.recvPhone">
            <a-input v-model:value="formData.recvPhone" placeholder="请输入收票电话" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="收票地址" v-bind="validateInfos.recvAddress">
            <a-input v-model:value="formData.recvAddress" placeholder="请输入收票地址" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="邮编" v-bind="validateInfos.zipcode">
	          <a-input-number v-model:value="formData.zipcode" placeholder="请输入邮编" style="width: 100%" :disabled="disabled"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

		<!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated>
      <a-tab-pane tab="供应商联系人" key="supplierContact" :forceRender="true">
        <j-vxe-table
          :keep-source="true"
          resizable
          ref="supplierContactTableRef"
          :loading="supplierContactTable.loading"
          :columns="supplierContactTable.columns"
          :dataSource="supplierContactTable.dataSource"
          :height="340"
          :disabled="disabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"/>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive, computed, toRaw, onMounted } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useValidateAntFormAndTable } from '/@/hooks/system/useJvxeMethods';
  import { querySupplierContactListByMainId, queryDataById, saveOrUpdate } from '../SupplierManagement.api';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import {supplierContactColumns} from '../SupplierManagement.data';
  import JSearchSelect from '/@/components/Form/src/jeecg/components/JSearchSelect.vue';
  import JAreaLinkage from '/@/components/Form/src/jeecg/components/JAreaLinkage.vue';
  import { Form } from 'ant-design-vue';
  const useForm = Form.useForm;

  export default defineComponent({
    name: "SupplierManagementForm",
    components:{
      JSearchSelect,
      JAreaLinkage,
      JVxeTable,
    },
    props:{
      formDisabled:{
        type: Boolean,
        default: false
      },
      formData: { type: Object, default: ()=>{} },
      formBpm: { type: Boolean, default: true }
    },
    emits:['success'],
    setup(props, {emit}) {
      const loading = ref(false);
      const supplierContactTableRef = ref();
      const supplierContactTable = reactive<Record<string, any>>({
        loading: false,
        columns: supplierContactColumns,
        dataSource: []
      });
      const activeKey = ref('supplierContact');
      const formData = reactive<Record<string, any>>({
        id: '',
        supplierIndex: '',
        supplierName: '',
        city: '',
        contact: '',
        bankAccount: '',
        bankName: '',
        accountName: '',
        kpAddress: '',
        taxId: '',
        kpPhone: '',
        kpRemark: '',
        receiver: '',
        recvPhone: '',
        recvAddress: '',
        zipcode: '',
      });

      //表单验证
      const validatorRules = reactive({
        supplierName: [{ required: true, message: '请输入供应商名称!'},],
      });
      const {resetFields, validate, validateInfos} = useForm(formData, validatorRules, {immediate: true});
      const dbData = {};
      const formItemLayout = {
        labelCol: {xs: {span: 24}, sm: {span: 5}},
        wrapperCol: {xs: {span: 24}, sm: {span: 16}},
      };

      // 表单禁用
      const disabled = computed(()=>{
        if(props.formBpm === true){
          if(props.formData.disabled === false){
            return false;
          }else{
            return true;
          }
        }
        return props.formDisabled;
      });

      

      function add() {
        resetFields();
        supplierContactTable.dataSource = [];
      }

      async function edit(row) {
        //主表数据
        await queryMainData(row.id);
        //子表数据
        const supplierContactDataList = await querySupplierContactListByMainId(row['id']);
        supplierContactTable.dataSource = [...supplierContactDataList];
      }

      async function queryMainData(id) {
        const row = await queryDataById(id);
        Object.keys(row).map(k => {
          formData[k] = row[k];
        });
      }

      const {getSubFormAndTableData, transformData} = useValidateAntFormAndTable(activeKey, {
        'supplierContact': supplierContactTableRef,
      });

      async function getFormData() {
        await validate();
        return transformData(toRaw(formData))
      }

      async function submitForm() {
        const mainData = await getFormData();
        const subData = await getSubFormAndTableData();
        const values = Object.assign({}, dbData, mainData, subData);
        console.log('表单提交数据', values)
        const isUpdate = values.id ? true : false
        await saveOrUpdate(values, isUpdate);
        //关闭弹窗
        emit('success');
      }
      
      function setFieldsValue(values) {
        if(values){
          Object.keys(values).map(k=>{
            formData[k] = values[k];
          });
        }
      }

      /**
       * 值改变事件触发-树控件回调
       * @param key
       * @param value
       */
      function handleFormChange(key, value) {
        formData[key] = value;
      }


      return {
        supplierContactTableRef,
        supplierContactTable,
        validatorRules,
        validateInfos,
        activeKey,
        loading,
        formData,
        setFieldsValue,
        handleFormChange,
        formItemLayout,
        disabled,
        getFormData,
        submitForm,
        add,
        edit
      }
    }
  });
</script>