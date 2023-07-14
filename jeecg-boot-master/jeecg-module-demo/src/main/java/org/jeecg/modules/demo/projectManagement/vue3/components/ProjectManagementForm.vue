<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
          <a-tab-pane tab="项目应收" key="projectPaymentTerm" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="projectPaymentTerm"
              v-if="projectPaymentTermTable.show"
              :loading="projectPaymentTermTable.loading"
              :columns="projectPaymentTermTable.columns"
              :dataSource="projectPaymentTermTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
          <a-tab-pane tab="收款管理" key="projectPaymentRecv" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="projectPaymentRecv"
              v-if="projectPaymentRecvTable.show"
              :loading="projectPaymentRecvTable.loading"
              :columns="projectPaymentRecvTable.columns"
              :dataSource="projectPaymentRecvTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
    </a-tabs>

    <div style="width: 100%;text-align: center" v-if="!formDisabled">
      <a-button @click="handleSubmit" pre-icon="ant-design:check" type="primary">提 交</a-button>
    </div>
  </div>
</template>

<script lang="ts">

  import {BasicForm, useForm} from '/@/components/Form/index';
  import { computed, defineComponent, reactive, ref, unref } from 'vue';
  import {defHttp} from '/@/utils/http/axios';
  import { propTypes } from '/@/utils/propTypes';
  import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods';
  import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils';
  import {getBpmFormSchema,projectPaymentTermColumns,projectPaymentRecvColumns} from '../ProjectManagement.data';
  import {saveOrUpdate,projectPaymentTermList,projectPaymentRecvList} from '../ProjectManagement.api';

  export default defineComponent({
    name: "ProjectManagementForm",
    components:{
      BasicForm,
    },
    props:{
      formData: propTypes.object.def({}),
      formBpm: propTypes.bool.def(true),
    },
    setup(props){
      const [registerForm, { setFieldsValue, setProps }] = useForm({
        labelWidth: 150,
        schemas: getBpmFormSchema(props.formData),
        showActionButtonGroup: false,
        baseColProps: {span: 24}
      });

      const formDisabled = computed(()=>{
        if(props.formData.disabled === false){
          return false;
        }
        return true;
      });

      const refKeys = ref(['projectPaymentTerm', 'projectPaymentRecv', ]);
      const activeKey = ref('projectPaymentTerm');
      const projectPaymentTerm = ref();
      const projectPaymentRecv = ref();
      const tableRefs = {projectPaymentTerm, projectPaymentRecv, };
      const projectPaymentTermTable = reactive({
        loading: false,
        dataSource: [],
        columns:projectPaymentTermColumns,
        show: false
      })
      const projectPaymentRecvTable = reactive({
        loading: false,
        dataSource: [],
        columns:projectPaymentRecvColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          projectPaymentTermList: allValues.tablesValue[0].tableData,
          projectPaymentRecvList: allValues.tablesValue[1].tableData,
        }
      }

      //表单提交事件
      async function requestAddOrEdit(values) {
        await saveOrUpdate(values, true);
      }

      const queryByIdUrl = '/projectManagement/projectManagement/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        requestSubTableData(projectPaymentTermList, {id: data.id}, projectPaymentTermTable, ()=>{
          projectPaymentTermTable.show = true;
        });
        requestSubTableData(projectPaymentRecvList, {id: data.id}, projectPaymentRecvTable, ()=>{
          projectPaymentRecvTable.show = true;
        });
        //默认是禁用
        await setProps({disabled: formDisabled.value})
      }

      initFormData();

      return {
        registerForm,
        formDisabled,
        formRef,
        handleSubmit,
        activeKey,
        handleChangeTabs,
        projectPaymentTerm,
        projectPaymentRecv,
        projectPaymentTermTable,
        projectPaymentRecvTable,
      }
    }
  });
</script>