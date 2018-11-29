package com.github.julianps.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.Module;

/**
 *                                                  | |     | |
 * __   ____ ___   ___ __ ______ _ __ ___   ___   __| |_   _| | ___
 * \ \ / / _` \ \ / / '__|______| '_ ` _ \ / _ \ / _` | | | | |/ _ \
 *  \ V / (_| |\ V /| |         | | | | | | (_) | (_| | |_| | |  __/
 *   \_/ \__,_| \_/ |_|         |_| |_| |_|\___/ \__,_|\__,_|_|\___|
 *
 * @author jstuecker
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class VavrModule implements Module {

	public static final int INDEX_ZERO = 0;

	@Override
	public void setupModule(ModelMapper modelMapper) {
		modelMapper.getConfiguration().getConverters().add(INDEX_ZERO, new ValueConverter());
	}
}
