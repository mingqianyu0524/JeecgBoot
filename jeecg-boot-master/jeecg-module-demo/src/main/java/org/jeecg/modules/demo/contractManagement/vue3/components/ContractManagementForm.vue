<template>
  <div>
    <BasicForm @register="registerForm" ref="formRef"/>
    <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated  @change="handleChangeTabs">
          <a-tab-pane tab="合同应付" key="contractPaymentTerm" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="contractPaymentTerm"
              v-if="contractPaymentTermTable.show"
              :loading="contractPaymentTermTable.loading"
              :columns="contractPaymentTermTable.columns"
              :dataSource="contractPaymentTermTable.dataSource"
              :height="340"
              :rowNumber="true"
              :rowSelection="true"
              :disabled="formDisabled"
              :toolbar="true"
            />
          </a-tab-pane>
          <a-tab-pane tab="付款管理" key="contractPaymentRecv" :forceRender="true">
            <JVxeTable
              keep-source
              resizable
              ref="contractPaymentRecv"
              v-if="contractPaymentRecvTable.show"
              :loading="contractPaymentRecvTable.loading"
              :columns="contractPaymentRecvTable.columns"
              :dataSource="contractPaymentRecvTable.dataSource"
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
  import {getBpmFormSchema,contractPaymentTermColumns,contractPaymentRecvColumns} from '../ContractManagement.data';
  import {saveOrUpdate,contractPaymentTermList,contractPaymentRecvList} from '../ContractManagement.api';

  export default defineComponent({
    name: "ContractManagementForm",
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

      const refKeys = ref(['contractPaymentTerm', 'contractPaymentRecv', ]);
      const activeKey = ref('contractPaymentTerm');
      const contractPaymentTerm = ref();
      const contractPaymentRecv = ref();
      const tableRefs = {contractPaymentTerm, contractPaymentRecv, };
      const contractPaymentTermTable = reactive({
        loading: false,
        dataSource: [],
        columns:contractPaymentTermColumns,
        show: false
      })
      const contractPaymentRecvTable = reactive({
        loading: false,
        dataSource: [],
        columns:contractPaymentRecvColumns,
        show: false
      })

      const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys,validateSubForm);

      function classifyIntoFormData(allValues) {
        let main = Object.assign({}, allValues.formValue)
        return {
          ...main, // 展开
          contractPaymentTermList: allValues.tablesValue[0].tableData,
          contractPaymentRecvList: allValues.tablesValue[1].tableData,
        }
      }

      //表单提交事件
      async function requestAddOrEdit(values) {
        await saveOrUpdate(values, true);
      }

      const queryByIdUrl = '/contractManagement/contractManagement/queryById';
      async function initFormData(){
        let params = {id: props.formData.dataId};
        const data = await defHttp.get({url: queryByIdUrl, params});
        //设置表单的值
        await setFieldsValue({...data});
        requestSubTableData(contractPaymentTermList, {id: data.id}, contractPaymentTermTable, ()=>{
          contractPaymentTermTable.show = true;
        });
        requestSubTableData(contractPaymentRecvList, {id: data.id}, contractPaymentRecvTable, ()=>{
          contractPaymentRecvTable.show = true;
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
        contractPaymentTerm,
        contractPaymentRecv,
        contractPaymentTermTable,
        contractPaymentRecvTable,
      }
    }
  });
</script>