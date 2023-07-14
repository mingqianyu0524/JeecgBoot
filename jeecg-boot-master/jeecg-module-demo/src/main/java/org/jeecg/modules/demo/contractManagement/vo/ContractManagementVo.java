package org.jeecg.modules.demo.contractManagement.vo;

import lombok.Data;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentRecv;
import org.jeecg.modules.demo.contractManagement.entity.ContractPaymentTerm;
import java.util.List;

@Data
public class ContractManagementVo {
    private String contractIndex;

    private String contractName;

    private String supplierName;

    private List<ContractPaymentTerm> contractPaymentTermList;

    private List<ContractPaymentRecv> contractPaymentRecvList;
}
