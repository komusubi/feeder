/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.komusubi.feeder.model;

import org.apache.commons.lang3.StringUtils;
import org.komusubi.feeder.model.Message.Script;

/**
 * @author jun.ozeki
 */
public abstract class AbstractScript implements Script {

    private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @author jun.ozeki
	 */
//	public interface Fragment {
//		String get();
//	}

    /**
     * compare script that call the each line method.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Script) {
            return this.line().equals(((Script) obj).line());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((line() == null) ? 0 : line().hashCode());
        return result;
    }

    public int length() {
        return line().length();
    }

    /**
     * @see org.komusubi.feeder.model.Message.Script#trimedLine()
     */
    @Override
    public String trimedLine() {
        return StringUtils.strip(line());
    }
    
    @Override
    public boolean isFragment() {
        return false;
    }
    
    @Override
    public String fragment() {
        return null;
    }
}
