<template>
  <BasicModal v-bind="$attrs" @register="registerModal" :title="title" :width="800" @ok="handleSubmit">
    <project-management-form ref="formComponent" :formDisabled="formDisabled" :formBpm="false" @success="submitSuccess"></project-management-form>
  </BasicModal>
</template>

<script lang="ts">
  import { ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import ProjectManagementForm from './ProjectManagementForm.vue';

  export default {
    name: "TestCgMainVxeModal",
    components:{
      BasicModal,
      ProjectManagementForm
    },
    emits:['register','success'],
    setup(_p, {emit}){
      const formComponent = ref()
      const isUpdate = ref(true);
      const formDisabled = ref(false);
      const title = ref('')

      //表单赋值
      const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
        setModalProps({confirmLoading: false,showCancelBtn:data?.showFooter,showOkBtn:data?.showFooter});
        isUpdate.value = !!data?.isUpdate;
        title.value = data?.isUpdate?'编辑':'新增'
        formDisabled.value = !data?.showFooter;
        if (unref(isUpdate)) {
          console.log('data', data)
          formComponent.value.edit(data.record)
        }else{
          formComponent.value.add()
        }
      });

      function handleSubmit() {
        formComponent.value.submitForm();
      }

      function submitSuccess(){
        emit('success');
        closeModal();
      }

      return {
        registerModal,
        title,
        formComponent,
        formDisabled,
        handleSubmit,
        submitSuccess
      }
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