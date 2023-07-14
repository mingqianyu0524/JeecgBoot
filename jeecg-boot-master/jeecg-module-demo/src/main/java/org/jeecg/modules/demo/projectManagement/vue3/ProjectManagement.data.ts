import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '序号',
    align:"center",
    dataIndex: 'indexId'
   },
   {
    title: '部门',
    align:"center",
    dataIndex: 'deptName_dictText'
   },
   {
    title: '项目年限',
    align:"center",
    dataIndex: 'projectYear_dictText'
   },
   {
    title: '项目编号',
    align:"center",
    dataIndex: 'projectIndex'
   },
   {
    title: '项目名称',
    align:"center",
    dataIndex: 'projectName'
   },
   {
    title: '项目分类',
    align:"center",
    dataIndex: 'projectCategory'
   },
   {
    title: '项目状态',
    align:"center",
    dataIndex: 'projectStatus_dictText'
   },
   {
    title: '客户名称',
    align:"center",
    dataIndex: 'clientName'
   },
   {
    title: '合同签订时间',
    align:"center",
    dataIndex: 'signDate',
    customRender:({text}) =>{
      return !text?"":(text.length>10?text.substr(0,10):text)
    },
   },
   {
    title: '合同周期',
    align:"center",
    dataIndex: 'contractPeriod'
   },
   {
    title: '合同应付',
    align:"center",
    dataIndex: 'contractPaymentTerm'
   },
   {
    title: '合同实付',
    align:"center",
    dataIndex: 'contractPaymentRecv'
   },
   {
    title: '专家费',
    align:"center",
    dataIndex: 'consultFee'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "部门",
      field: "deptName",
      component: 'JSelectDept',
      colProps: {span: 6},
 	},
	{
      label: "项目年限",
      field: "projectYear",
      component: 'JDictSelectTag',
      componentProps:{
          dictCode:"project_year"
      },
      colProps: {span: 6},
 	},
	{
      label: "项目编号",
      field: "projectIndex",
      component: 'Input',
      colProps: {span: 6},
 	},
	{
      label: "项目名称",
      field: "projectName",
      component: 'Input',
      colProps: {span: 6},
 	},
	{
      label: "项目分类",
      field: "projectCategory",
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_project_categories",
            fieldConfig: [
                { source: 'name', target: 'projectCategory' },
            ],
            multi:true
        }
    },

      colProps: {span: 6},
 	},
	{
      label: "项目状态",
      field: "projectStatus",
      component: 'JDictSelectTag',
      componentProps:{
          dictCode:"project_status"
      },
      colProps: {span: 6},
 	},
	{
      label: "客户名称",
      field: "clientName",
      component: 'Input',
      colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '部门',
    field: 'deptName',
     component: 'JSelectDept',
  },
  {
    label: '项目年限',
    field: 'projectYear',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"project_year"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目年限!'},
          ];
     },
  },
  {
    label: '项目编号',
    field: 'projectIndex',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目编号!'},
          ];
     },
  },
  {
    label: '项目名称',
    field: 'projectName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目名称!'},
          ];
     },
  },
  {
    label: '项目分类',
    field: 'projectCategory',
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_project_categories",
            fieldConfig: [
                { source: 'name', target: 'projectCategory' },
            ],
            multi:true
        }
    },

    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目分类!'},
          ];
     },
  },
  {
    label: '项目状态',
    field: 'projectStatus',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"project_status"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目状态!'},
          ];
     },
  },
  {
    label: '客户名称',
    field: 'clientName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入客户名称!'},
          ];
     },
  },
  {
    label: '合同签订时间',
    field: 'signDate',
    component: 'DatePicker',
  },
  {
    label: '合同周期',
    field: 'contractPeriod',
    component: 'Input',
  },
  {
    label: '合同扫描件',
    field: 'contractScanned',
     component: 'JImageUpload',
     componentProps:{
      },
  },
  {
    label: '专家费',
    field: 'consultFee',
    component: 'InputNumber',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表单数据
//子表表格配置
export const projectPaymentTermColumns: JVxeColumn[] = [
    {
      title: '说明',
      key: 'spec',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '金额',
      key: 'paymentAmount',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'remark',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]
export const projectPaymentRecvColumns: JVxeColumn[] = [
    {
      title: '收款流水号',
      key: 'paymentSerialNumber',
      type: JVxeTypes.input,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '项目编号',
      key: 'projectIndex',
      type: JVxeTypes.popup,
      popupCode:"select_all_projects",
      fieldConfig: [
        { source: 'id', target: 'projectFkId' },
        { source: 'project_index', target: 'projectIndex' },
        { source: 'project_name', target: 'projectName' },
        { source: 'client_name', target: 'clientName' },
      ],

      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '项目名称',
      key: 'projectName',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '客户名称',
      key: 'clientName',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '收款金额',
      key: 'paymentAmount',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '开票日期',
      key: 'kpDate',
      type: JVxeTypes.date,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '到款日期',
      key: 'recvDate',
      type: JVxeTypes.date,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '备注',
      key: 'remark',
      type: JVxeTypes.textarea,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
// 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}