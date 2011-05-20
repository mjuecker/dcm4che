/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/gunterze/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che.net.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 *
 */
public class InstanceRefs implements Serializable, Iterable<String[]> {

    private static final long serialVersionUID = 8788279917089668764L;

    private final ArrayList<String[]> list;

    public InstanceRefs() {
        this(10);
    }

    public InstanceRefs(int initialCapacity) {
        list = new ArrayList<String[]>(initialCapacity);
    }

    public void add(String iuid, String cuid, String tsuid, String uri) {
        list.add(new String[] { iuid, cuid, tsuid, uri } );
    }

    public Iterator<String[]> iterator() {
        return list.iterator();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public String[][] getTransferCapabilities() {
        HashMap<String, HashSet<String>> map =
            new HashMap<String, HashSet<String>>();
        for (String[] uids : list) {
            HashSet<String> tss = map.get(uids[1]);
            if (tss == null)
                map.put(uids[1], tss = new HashSet<String>(4));
            tss.add(uids[2]);
        }
        String[][] tcs = new String[map.size()][];
        Set<Entry<String, HashSet<String>>> entrySet = map.entrySet();
        for (Entry<String, HashSet<String>> entry : entrySet) {
            String cuid = entry.getKey();
            HashSet<String> tss = entry.getValue();
            String[] tc = new String[tss.size() + 1];
            tc[0] = cuid;
            Iterator<String> iter = tss.iterator();
            for (int i = 1; i < tc.length; i++)
                tc[i] = iter.next();
        }
        return tcs ;
    }
}
