<template>
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :title="title" :width="800" @ok="handleSubmit">
      <BasicForm @register="registerForm" ref="formRef"/>
  <!-- 子表单区域 -->
    <a-tabs v-model:activeKey="activeKey" animated @change="handleChangeTabs">
      <a-tab-pane tab="合同应付" key="contractPaymentTerm" :forceRender="true">
        <JVxeTable
          keep-source
          resizable
          ref="contractPaymentTerm"
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
  </BasicModal>
</template>

<script lang="ts" setup>
    import {ref, computed, unref,reactive} from 'vue';
    import {BasicModal, useModalInner} from '/@/components/Modal';
    import {BasicForm, useForm} from '/@/components/Form/index';
    import { JVxeTable } from '/@/components/jeecg/JVxeTable'
    import { useJvxeMethod } from '/@/hooks/system/useJvxeMethods.ts'
    import {formSchema,contractPaymentTermColumns,contractPaymentRecvColumns} from '../ContractManagement.data';
    import {saveOrUpdate,contractPaymentTermList,contractPaymentRecvList} from '../ContractManagement.api';
    import { VALIDATE_FAILED } from '/@/utils/common/vxeUtils'
    // Emits声明
    const emit = defineEmits(['register','success']);
    const isUpdate = ref(true);
    const formDisabled = ref(false);
    const refKeys = ref(['contractPaymentTerm', 'contractPaymentRecv', ]);
    const activeKey = ref('contractPaymentTerm');
    const contractPaymentTerm = ref();
    const contractPaymentRecv = ref();
    const tableRefs = {contractPaymentTerm, contractPaymentRecv, };
    const contractPaymentTermTable = reactive({
          loading: false,
          dataSource: [],
          columns:contractPaymentTermColumns
    })
    const contractPaymentRecvTable = reactive({
          loading: false,
          dataSource: [],
          columns:contractPaymentRecvColumns
    })
    //表单配置
    const [registerForm, {setProps,resetFields, setFieldsValue, validate}] = useForm({
        //labelWidth: 150,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: {span: 24}
    });
     //表单赋值
    const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
        //重置表单
        await reset();
        setModalProps({confirmLoading: false,showCancelBtn:data?.showFooter,showOkBtn:data?.showFooter});
        isUpdate.value = !!data?.isUpdate;
        formDisabled.value = !data?.showFooter;
        if (unref(isUpdate)) {
            //表单赋值
            await setFieldsValue({
                ...data.record,
            });
             requestSubTableData(contractPaymentTermList, {id:data?.record?.id}, contractPaymentTermTable)
             requestSubTableData(contractPaymentRecvList, {id:data?.record?.id}, contractPaymentRecvTable)
        }
        // 隐藏底部时禁用整个表单
       setProps({ disabled: !data?.showFooter })
    });
    //方法配置
    const [handleChangeTabs,handleSubmit,requestSubTableData,formRef] = useJvxeMethod(requestAddOrEdit,classifyIntoFormData,tableRefs,activeKey,refKeys);

    //设置标题
    const title = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

    async function reset(){
      await resetFields();
      activeKey.value = 'contractPaymentTerm';
      contractPaymentTermTable.dataSource = [];
      contractPaymentRecvTable.dataSource = [];
    }
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
        try {
            setModalProps({confirmLoading: true});
            //提交表单
            await saveOrUpdate(values, isUpdate.value);
            //关闭弹窗
            closeModal();
            //刷新列表
            emit('success');
        } finally {
            setModalProps({confirmLoading: false});
        }
    }
</script>

<style lang="less" scoped>
	/** 时间和数字输入框样式 */
    :deep(.ant-input-number){
		width: 100%
	}

	:deep(.ant-calendar-picker){
		width: 100%
	}
</style>