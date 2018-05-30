/**
 * Copyright Dingxuan. All Rights Reserved.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import shim.ISmartContract;
import shim.ISmartContractStub;
import shim.SmartContractBase;

/**
 * 类描述
 *
 * @author wanliangbing
 * @date 2018/05/26
 * @company Dingxuan
 */
public class MyCC extends SmartContractBase {

  @Override
  public SmartContractResponse init(ISmartContractStub stub) {
    System.out.println("mcc init");
    return newSuccessResponse();
  }

  @Override
  public SmartContractResponse invoke(ISmartContractStub stub) {
    System.out.println("mcc invoke");
    return newSuccessResponse();
  }

  @Override
  public String getSmartContractStrDescription() {
    return "MyCC";
  }
}
