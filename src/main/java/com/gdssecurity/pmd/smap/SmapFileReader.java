/*
(C) Copyright  2014-2015 Alberto Fernández <infjaf@gmail.com>
(C) Copyright  2012      Gotham Digital Science, LLC -- All Rights Reserved
 
Unless explicitly acquired and licensed from Licensor under another
license, the contents of this file are subject to the Reciprocal Public
License ("RPL") Version 1.5, or subsequent versions as allowed by the RPL,
and You may not copy or use this file in either source code or executable
form, except in compliance with the terms and conditions of the RPL.

All software distributed under the RPL is provided strictly on an "AS
IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT
LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific
language governing rights and limitations under the RPL. 

This code is licensed under the Reciprocal Public License 1.5 (RPL1.5)
http://www.opensource.org/licenses/rpl1.5

 */

package com.gdssecurity.pmd.smap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SmapFileReader implements SmapReader {

	private File file;

	public SmapFileReader(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		if (this.file != null) {
			return "SmapFileReader:" + this.file.toString();
		}
		return "SmapFileReader";
	}

	@Override
	public List<String> readSmap() {
		if (this.file != null) {
			try {
				return Files.readAllLines(this.file.toPath(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				return new ArrayList<String>();
			}

		}
		return new ArrayList<String>();
	}

}
