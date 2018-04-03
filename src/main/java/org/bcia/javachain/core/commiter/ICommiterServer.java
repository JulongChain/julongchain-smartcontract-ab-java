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
package org.bcia.javachain.core.commiter;

import org.bcia.javachain.core.ledger.*;
import org.bcia.javachain.protos.common.*;

import java.util.Map;

/**
 * 确认服务接口
 *
 * @author  wanglei
 * @date 18-3-27
 * @company Dingxuan
 */
public interface ICommiterServer {

    //TODO :相关对象未定义
    void CommitWithPvtData(BlockAndPvtData blockAndPvtData) throws Exception;

    BlockAndPvtData GetPvtDataAndBlockByNum(Integer seqNumber) throws Exception;

    TxPvtData GetPvtDataByNum(Integer blockNumber, Map<String, Map<String, Boolean> > filter) throws  Exception;

    Integer  LedgerHeight() throws Exception;

    Common.Block GetBlocks(Integer[] blockSeqs) throws Exception;

    void Close();
}