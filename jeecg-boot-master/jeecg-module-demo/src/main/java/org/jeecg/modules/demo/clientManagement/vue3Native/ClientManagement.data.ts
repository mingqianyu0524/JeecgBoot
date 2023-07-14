import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '客户名称',
    align:"center",
    dataIndex: 'clientName'
   },
   {
    title: '所在城市',
    align:"center",
    dataIndex: 'city',
    slots: { customRender: 'pcaSlot' },
   },
   {
    title: '联系人',
    align:"center",
    dataIndex: 'contact_dictText'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '客户编号',
    field: 'clientIndex',
    component: 'InputNumber',
    dynamicDisabled:true
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
    label: '所在城市',
    field: 'city',
    component: 'JAreaLinkage',
  },
  {
    label: '联系人',
    field: 'contact',
    component: 'JSearchSelect',
    componentProps:{
       dict:"client_contact,name,name"
    },
  },
  {
    label: '银行账号',
    field: 'bankAccount',
    component: 'Input',
  },
  {
    label: '开户银行',
    field: 'bankName',
    component: 'Input',
  },
  {
    label: '开户名称',
    field: 'accountName',
    component: 'Input',
  },
  {
    label: '开票地址',
    field: 'kpAddress',
    component: 'Input',
  },
  {
    label: '税号',
    field: 'taxId',
    component: 'Input',
  },
  {
    label: '开票电话',
    field: 'kpPhone',
    component: 'Input',
  },
  {
    label: '开票要求',
    field: 'kpRemark',
    component: 'InputTextArea',
  },
  {
    label: '收票人',
    field: 'receiver',
    component: 'Input',
  },
  {
    label: '收票电话',
    field: 'recvPhone',
    component: 'Input',
  },
  {
    label: '收票地址',
    field: 'recvAddress',
    component: 'Input',
  },
  {
    label: '邮编',
    field: 'zipcode',
    component: 'InputNumber',
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
export const clientContactColumns: JVxeColumn[] = [
    {
      title: '姓名',
      key: 'name',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '性别',
      key: 'sex',
      type: JVxeTypes.select,
      options:[],
      dictCode:"sex",
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '生日',
      key: 'birthday',
      type: JVxeTypes.date,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '职务',
      key: 'duty',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '联系电话',
      key: 'phone',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: false},
          { pattern: "m", message: "${title}格式不正确" }
        ],
    },
    {
      title: '邮件',
      key: 'email',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: false},
          { pattern: "e", message: "${title}格式不正确" }
        ],
    },
    {
      title: 'QQ',
      key: 'qq',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '工作地址',
      key: 'workAddress',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]
