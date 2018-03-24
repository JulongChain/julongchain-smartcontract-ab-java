/**
 * Copyright Dingxuan. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.javachain.core.smartcontract;

import com.google.protobuf.ByteString;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.common.util.proto.ProposalResponseUtils;
import org.bcia.javachain.core.common.smartcontractprovider.SmartContractContext;
import org.bcia.javachain.protos.common.Common;
import org.bcia.javachain.protos.node.ProposalResponsePackage;
import org.bcia.javachain.protos.node.Smartcontract;
import org.bcia.javachain.protos.node.SmartcontractShim;

/**
 * 智能合约执行器
 *
 * @author zhouhui
 * @date 2018/3/22
 * @company Dingxuan
 */
public class SmartContractExecutor {
    private static JavaChainLog log = JavaChainLogFactory.getLog(SmartContractExecutor.class);

    private SmartContractSupport scSupport = new SmartContractSupport();

    /**
     * 执行智能合约
     *
     * @param scContext
     * @param spec
     * @return
     */
    public ProposalResponsePackage.Response execute(SmartContractContext scContext, Object spec) {
        //TODO:测试数据
        long timeout = 3000;

        int msgType = 0;
        //spec必须为SmartContractDeploymentSpec或者SmartContractInvocationSpec实例
        if (spec instanceof Smartcontract.SmartContractDeploymentSpec) {
            //消息类型为初始化
            msgType = SmartcontractShim.SmartContractMessage.Type.INIT_VALUE;
        } else if (spec instanceof Smartcontract.SmartContractInvocationSpec) {
            //消息类型为交易
            msgType = SmartcontractShim.SmartContractMessage.Type.TRANSACTION_VALUE;
        } else {
            log.error("Unsupported spec");
            return ProposalResponseUtils.buildErrorResponse(Common.Status.INTERNAL_SERVER_ERROR, "Unsupported spec");
        }

        //启动智能合约
        Smartcontract.SmartContractInput scInput = scSupport.launch(scContext, spec);
        if (scInput == null) {
            log.error("launch smart contract fail");
            return ProposalResponseUtils.buildErrorResponse(Common.Status.INTERNAL_SERVER_ERROR, "launch smart contract fail");
        }

        //TODO:SmartContractInput是否需要再处理?

        SmartcontractShim.SmartContractMessage scMessage = buildSmartContractMessage(msgType, scInput.toByteArray(),
                scContext.getTxID(), scContext.getChainID());
        //执行智能合约
        SmartcontractShim.SmartContractMessage responseMessage = scSupport.execute(scContext, scMessage, timeout);

        //判断返回结果
        if (responseMessage != null) {
            //完成时返回负载
            if (responseMessage.getType().equals(SmartcontractShim.SmartContractMessage.Type.COMPLETED)) {
                return ProposalResponseUtils.buildResponse(responseMessage.getPayload());
            } else {
                return ProposalResponseUtils.buildErrorResponse(Common.Status.INTERNAL_SERVER_ERROR,
                        "execute smart contract fail: " + responseMessage.getPayload());
            }
        }

        return ProposalResponseUtils.buildErrorResponse(Common.Status.INTERNAL_SERVER_ERROR, "Unknow error");
    }

    /**
     * 构造智能合约消息对象
     *
     * @param msgType
     * @param payload
     * @param txId
     * @param groupId
     * @return
     */
    private SmartcontractShim.SmartContractMessage buildSmartContractMessage(int msgType, byte[] payload, String
            txId, String groupId) {
        SmartcontractShim.SmartContractMessage.Builder scMessageBuilder = SmartcontractShim.SmartContractMessage
                .newBuilder();

        scMessageBuilder.setTypeValue(msgType);
        scMessageBuilder.setPayload(ByteString.copyFrom(payload));
        scMessageBuilder.setTxid(txId);
        scMessageBuilder.setGroupId(groupId);

        return scMessageBuilder.build();
    }
}
