// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.designer.fileoutputxml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.components.IODataComponent;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.AbstractExternalNode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IComponentDocumentation;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.temp.ECodePart;
import org.talend.designer.codegen.ICodeGeneratorService;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 * $Id: FileOutputXMLComponent.java,v 1.1 2007/06/12 07:20:37 gke Exp $
 * 
 */
public class FileOutputXMLComponent extends AbstractExternalNode {

    public static final String ROOT_TAGS = "ROOT_TAGS"; //$NON-NLS-1$

    public static final String TAG = "TAG"; //$NON-NLS-1$

    public static final String MAPPING = "MAPPING"; //$NON-NLS-1$

    public static final String COLUMN = "COLUMN"; //$NON-NLS-1$

    public static final String ATTRIBUTE = "ATTRIBUTE"; //$NON-NLS-1$

    public static final String LABEL = "LABEL"; //$NON-NLS-1$

    public static final String DEPTH = "DEPTH"; //$NON-NLS-1$

    private FOXMain foxmain;

    public FileOutputXMLComponent() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#initialize()
     */
    public void initialize() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getComponentDocumentation(java.lang.String, java.lang.String)
     */
    public IComponentDocumentation getComponentDocumentation(String componentName, String tempFolderPath) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#getExternalData()
     */
    public IExternalData getExternalData() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#loadDataIn(java.io.InputStream, java.io.Reader)
     */
    public void loadDataIn(InputStream inputStream, Reader reader) throws IOException, ClassNotFoundException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#loadDataOut(java.io.OutputStream, java.io.Writer)
     */
    public void loadDataOut(OutputStream out, Writer writer) throws IOException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.eclipse.swt.widgets.Composite)
     */
    public int open(Composite parent) {
        return open(parent.getDisplay());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#open(org.eclipse.swt.widgets.Display)
     */
    public int open(Display display) {
        foxmain = new FOXMain(this);
        Shell shell = foxmain.createUI(display);
        while (!shell.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            } catch (Throwable e) {
                if (foxmain.isStandAloneMode()) {
                    e.printStackTrace();
                } else {
                    ExceptionHandler.process(e);
                }
            }
        }
        if (foxmain.isStandAloneMode()) {
            display.dispose();
        }
        return foxmain.getFoxManager().getUiManager().getUiResponse();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IExternalNode#setExternalData(org.talend.core.model.process.IExternalData)
     */
    public void setExternalData(IExternalData persistentData) {
        // do nothing here
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.AbstractExternalNode#renameMetadataColumnName(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    protected void renameMetadataColumnName(String conectionName, String oldColumnName, String newColumnName) {
    }

    @Override
    public void metadataInputChanged(IODataComponent dataComponent, String connectionToApply) {
        List<Map<String, String>> list = (List<Map<String, String>>) this.getElementParameter(MAPPING).getValue();
        boolean[] flags = new boolean[list.size()];

        for (ColumnNameChanged col : dataComponent.getColumnNameChanged()) {
            System.out.println("    -> " + col + " " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$
            for (int i = 0; i < list.size(); i++) {
                if (!flags[i] && list.get(i).get(COLUMN).equals(col.getOldName())) {
                    list.get(i).put(COLUMN, col.getNewName());
                    flags[i] = true;
                }
            }
        }

        for (int i = 0; i < flags.length; i++) {
            if (flags[i]) {
                this.getElementParameter(MAPPING).setValue(list);
                break;
            }
        }

    }

    @Override
    public void metadataOutputChanged(IODataComponent dataComponent, String connectionToApply) {
        List<Map<String, String>> list = (List<Map<String, String>>) this.getElementParameter(MAPPING).getValue();
        boolean[] flags = new boolean[list.size()];

        for (ColumnNameChanged col : dataComponent.getColumnNameChanged()) {
            System.out.println("    -> " + col + " " + connectionToApply); //$NON-NLS-1$ //$NON-NLS-2$
            for (int i = 0; i < list.size(); i++) {
                if (!flags[i] && list.get(i).get(COLUMN).equals(col.getOldName())) {
                    list.get(i).put(COLUMN, col.getNewName());
                    flags[i] = true;
                }
            }
        }

        for (int i = 0; i < flags.length; i++) {
            if (flags[i]) {
                this.getElementParameter(MAPPING).setValue(list);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public boolean setTableElementParameter(List<Map<String, String>> epsl, String paraName) {
        List<IElementParameter> eps = (List<IElementParameter>) this.getElementParameters();
        boolean result = false;
        for (int i = 0; i < eps.size(); i++) {
            IElementParameter parameter = eps.get(i);
            if (parameter.getField() == EParameterFieldType.TABLE && parameter.getName().equals(paraName)) {
                List<Map<String, String>> newValues = new ArrayList<Map<String, String>>();
                for (Map<String, String> map : epsl) {
                    Map<String, String> newMap = new HashMap<String, String>();
                    newMap.putAll(map);
                    newValues.add(newMap);
                }
                List<Map<String, String>> oldValues = (List<Map<String, String>>) parameter.getValue();
                if (paraName.equals(ROOT_TAGS)) {
                    if (oldValues.size() != newValues.size()) {
                        result = true;
                    } else {
                        for (int k = 0; k < oldValues.size(); k++) {
                            if (!oldValues.get(k).get(TAG).equals(newValues.get(k).get(TAG))) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
                if (paraName.equals(MAPPING)) {
                    if (oldValues.size() != newValues.size()) {
                        result = true;
                    } else {
                        for (int k = 0; k < oldValues.size(); k++) {
                            if (!oldValues.get(k).get(COLUMN).equals(newValues.get(k).get(COLUMN))) {
                                result = true;
                                break;
                            }
                            if (!oldValues.get(k).get(ATTRIBUTE).equals(newValues.get(k).get(ATTRIBUTE))) {
                                result = true;
                                break;
                            }
                            if (!oldValues.get(k).get(LABEL).equals(newValues.get(k).get(LABEL))) {
                                result = true;
                                break;
                            }
                            if (!oldValues.get(k).get(DEPTH).equals(newValues.get(k).get(DEPTH))) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
                if (result) {
                    parameter.setValue(newValues);
                }
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public List<Map<String, String>> getTableList(String paraName) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<IElementParameter> eps = (List<IElementParameter>) this.getElementParameters();
        if (eps == null) {
            return list;
        }
        for (int i = 0; i < eps.size(); i++) {
            IElementParameter parameter = eps.get(i);
            if (parameter.getField() == EParameterFieldType.TABLE && parameter.getName().equals(paraName)) {
                list = (List<Map<String, String>>) parameter.getValue();
                break;
            }
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.INode#getGeneratedCode()
     */
    public String getGeneratedCode() {
        try {
            ICodeGeneratorService service = FOXPlugin.getDefault().getCodeGeneratorService();
            return service.createCodeGenerator().generateComponentCode(this, ECodePart.MAIN);
        } catch (SystemException e) {
            ExceptionHandler.process(e);
        }
        return ""; //$NON-NLS-1$
    }

    public void renameInputConnection(String oldName, String newName) {

    }

    public void renameOutputConnection(String oldName, String newName) {

    }

    /**
     * DOC gke Comment method "getMetadataTable".
     * 
     * @return
     */
    public IMetadataTable getMetadataTable() {
        try {
            return getMetadataList().get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
