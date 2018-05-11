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
package org.bcia.javachain.common.groupconfig.capability;

import org.bcia.javachain.protos.common.Configuration;

import java.util.Map;

/**
 * 对象
 *
 * @author zhouhui
 * @date 2018/5/10
 * @company Dingxuan
 */
public class GroupProvider implements IGroupCapabilities {
    private Map<String, Configuration.Capability> capabilityMap;

    private boolean supported;
    private int mspVersion;

    public GroupProvider(Map<String, Configuration.Capability> capabilityMap) {
        this.capabilityMap = capabilityMap;

        this.supported = true;
        this.mspVersion = 0;
    }

    @Override
    public boolean isSupported() {
        return supported;
    }

    @Override
    public int getMspVersion() {
        return mspVersion;
    }
}
