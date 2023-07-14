<template>
  <a-spin :spinning="loading">
    <a-form v-bind="formItemLayout">
      <a-row>
        <a-col :span="24">
          <a-form-item label="部门" v-bind="validateInfos.deptName">
	          <j-select-dept v-model:value="formData.deptName" :multiple="true" checkStrictly :disabled="disabled" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="项目年限" v-bind="validateInfos.projectYear">
	          <j-dict-select-tag v-model:value="formData.projectYear" dictCode="project_year" placeholder="请选择项目年限" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="项目编号" v-bind="validateInfos.projectIndex">
            <a-input v-model:value="formData.projectIndex" placeholder="请输入项目编号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="项目名称" v-bind="validateInfos.projectName">
            <a-input v-model:value="formData.projectName" placeholder="请输入项目名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="项目分类" v-bind="validateInfos.projectCategory">
		        <j-popup
		          placeholder="请选择项目分类"
		          v-model:value="formData.projectCategory"
		          code="select_project_categories"
		          :fieldConfig="[
		            { source: 'name', target: 'projectCategory' },
		          ]"
		          :multi="true"
		          :setFieldsValue="setFieldsValue"
              :disabled="disabled"		        />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="项目状态" v-bind="validateInfos.projectStatus">
	          <j-dict-select-tag v-model:value="formData.projectStatus" dictCode="project_status" placeholder="请选择项目状态" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="客户名称" v-bind="validateInfos.clientName">
            <a-input v-model:value="formData.clientName" placeholder="请输入客户名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="合同签订时间" v-bind="validateInfos.signDate">
		        <a-date-picker placeholder="请选择合同签订时间" v-model:value="formData.signDate" value-format="YYYY-MM-DD"  style="width: 100%" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="合同周期" v-bind="validateInfos.contractPeriod">
            <a-input v-model:value="formData.contractPeriod" placeholder="请输入合同周期" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="合同扫描件" v-bind="validateInfos.contractScanned">
	          <j-image-upload  v-model:value="formData.contractScanned" :disabled="disabled"></j-image-upload>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="专家费" v-bind="validateInfos.consultFee">
	          <a-input-number v-model:value="formData.consultFee" placeholder="请输入专家费" style="width: 100%" :disabled="disabled"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="备注" v-bind="validateInfos.remark">
	          <a-textarea v-model:value="formData.remark" rows="4" placeholder="请输入备注" :disabled="disabled"/>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>

		<!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated>
      <a-tab-pane tab="项目应收" key="projectPaymentTerm" :forceRender="true">
        <j-vxe-table
          :keep-source="true"
          resizable
          ref="projectPaymentTermTableRef"
          :loading="projectPaymentTermTable.loading"
          :columns="projectPaymentTermTable.columns"
          :dataSource="projectPaymentTermTable.dataSource"
          :height="340"
          :disabled="disabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"/>
      </a-tab-pane>
      <a-tab-pane tab="收款管理" key="projectPaymentRecv" :forceRender="true">
        <j-vxe-table
          :keep-source="true"
          resizable
          ref="projectPaymentRecvTableRef"
          :loading="projectPaymentRecvTable.loading"
          :columns="projectPaymentRecvTable.columns"
          :dataSource="projectPaymentRecvTable.dataSource"
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
  import { queryProjectPaymentTermListByMainId, queryProjectPaymentRecvListByMainId, queryDataById, saveOrUpdate } from '../ProjectManagement.api';
  import { JVxeTable } from '/@/components/jeecg/JVxeTable';
  import {projectPaymentTermColumns, projectPaymentRecvColumns} from '../ProjectManagement.data';
  import JDictSelectTag from '/@/components/Form/src/jeecg/components/JDictSelectTag.vue';
  import JPopup from '/@/components/Form/src/jeecg/components/JPopup.vue';
  import JSelectDept from '/@/components/Form/src/jeecg/components/JSelectDept.vue';
  import JImageUpload from '/@/components/Form/src/jeecg/components/JImageUpload.vue';
  import { Form } from 'ant-design-vue';
  const useForm = Form.useForm;

  export default defineComponent({
    name: "ProjectManagementForm",
    components:{
      JDictSelectTag,
      JPopup,
      JSelectDept,
      JImageUpload,
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
      const projectPaymentTermTableRef = ref();
      const projectPaymentTermTable = reactive<Record<string, any>>({
        loading: false,
        columns: projectPaymentTermColumns,
        dataSource: []
      });
      const projectPaymentRecvTableRef = ref();
      const projectPaymentRecvTable = reactive<Record<string, any>>({
        loading: false,
        columns: projectPaymentRecvColumns,
        dataSource: []
      });
      const activeKey = ref('projectPaymentTerm');
      const formData = reactive<Record<string, any>>({
        id: '',
        deptName: '',
        projectYear: '',
        projectIndex: '',
        projectName: '',
        projectCategory: '',
        projectStatus: '',
        clientName: '',
        signDate: '',
        contractPeriod: '',
        contractScanned: '',
        consultFee: '',
        remark: '',
      });

      //表单验证
      const validatorRules = reactive({
        projectYear: [{ required: true, message: '请输入项目年限!'},],
        projectIndex: [{ required: true, message: '请输入项目编号!'},],
        projectName: [{ required: true, message: '请输入项目名称!'},],
        projectCategory: [{ required: true, message: '请输入项目分类!'},],
        projectStatus: [{ required: true, message: '请输入项目状态!'},],
        clientName: [{ required: true, message: '请输入客户名称!'},],
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
        projectPaymentTermTable.dataSource = [];
        projectPaymentRecvTable.dataSource = [];
      }

      async function edit(row) {
        //主表数据
        await queryMainData(row.id);
        //子表数据
        const projectPaymentTermDataList = await queryProjectPaymentTermListByMainId(row['id']);
        projectPaymentTermTable.dataSource = [...projectPaymentTermDataList];
        const projectPaymentRecvDataList = await queryProjectPaymentRecvListByMainId(row['id']);
        projectPaymentRecvTable.dataSource = [...projectPaymentRecvDataList];
      }

      async function queryMainData(id) {
        const row = await queryDataById(id);
        Object.keys(row).map(k => {
          formData[k] = row[k];
        });
      }

      const {getSubFormAndTableData, transformData} = useValidateAntFormAndTable(activeKey, {
        'projectPaymentTerm': projectPaymentTermTableRef,
        'projectPaymentRecv': projectPaymentRecvTableRef,
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
        projectPaymentTermTableRef,
        projectPaymentTermTable,
        projectPaymentRecvTableRef,
        projectPaymentRecvTable,
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