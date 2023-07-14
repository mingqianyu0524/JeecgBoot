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
    title: '项目编号',
    align:"center",
    dataIndex: 'projectIndex'
   },
   {
    title: '合同名称',
    align:"center",
    dataIndex: 'contractName'
   },
   {
    title: '合同签订时间',
    align:"center",
    dataIndex: 'contractSignDate',
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
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "项目编号",
      field: "projectIndex",
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_all_projects",
            fieldConfig: [
                { source: 'id', target: 'projectFkId' },
                { source: 'project_index', target: 'projectIndex' },
                { source: 'client_name', target: 'clientName' },
            ],
            multi:true
        }
    },

      colProps: {span: 6},
 	},
	{
      label: "合同名称",
      field: "contractName",
      component: 'Input',
      colProps: {span: 6},
 	},
	{
      label: "合同签订时间",
      field: "contractSignDate",
      component: 'DatePicker',
      colProps: {span: 6},
 	},
	{
      label: "供应商名称",
      field: "supplierName",
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_supplier",
            fieldConfig: [
                { source: 'supplier_name', target: 'supplierName' },
            ],
            multi:true
        }
    },

      colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '部门',
    field: 'deptName',
     component: 'JSelectDept',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入部门!'},
          ];
     },
  },
  {
    label: '项目编号',
    field: 'projectIndex',
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_all_projects",
            fieldConfig: [
                { source: 'id', target: 'projectFkId' },
                { source: 'project_index', target: 'projectIndex' },
                { source: 'client_name', target: 'clientName' },
            ],
            multi:true
        }
    },

    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入项目编号!'},
          ];
     },
  },
  {
    label: '合同编号',
    field: 'contractIndex',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入合同编号!'},
          ];
     },
  },
  {
    label: '合同名称',
    field: 'contractName',
    component: 'Input',
  },
  {
    label: '合同签订时间',
    field: 'contractSignDate',
    component: 'DatePicker',
  },
  {
    label: '合同周期',
    field: 'contractPeriod',
    component: 'Input',
  },
  {
    label: '客户名称',
    field: 'clientName',
    component: 'Input',
  },
  {
    label: '供应商名称',
    field: 'supplierName',
    component: 'JPopup',
    componentProps: ({ formActionType }) => {
        const {setFieldsValue} = formActionType;
        return{
            setFieldsValue:setFieldsValue,
            code:"select_supplier",
            fieldConfig: [
                { source: 'supplier_name', target: 'supplierName' },
            ],
            multi:true
        }
    },

  },
  {
    label: '合同扫描件',
    field: 'contractScanned',
     component: 'JImageUpload',
     componentProps:{
      },
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
export const contractPaymentTermColumns: JVxeColumn[] = [
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
export const contractPaymentRecvColumns: JVxeColumn[] = [
    {
      title: '付款流水号',
      key: 'paymentSerialNumber',
      type: JVxeTypes.input,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '合同编号',
      key: 'contractIndex',
      type: JVxeTypes.popup,
      popupCode:"select_all_contracts",
      fieldConfig: [
        { source: 'contract_index', target: 'contractIndex' },
        { source: 'contract_name', target: 'contractName' },
        { source: 'id', target: 'contractFkId' },
        { source: 'project_index', target: 'projectIndex' },
        { source: 'project_name', target: 'projectName' },
      ],

      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '项目编号',
      key: 'projectIndex',
      type: JVxeTypes.input,
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
      title: '付款金额',
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
      title: '付款日期',
      key: 'payDate',
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