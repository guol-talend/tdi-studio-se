// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.rowgenerator.ui.editor;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.gef.commands.Command;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataSchema;
import org.talend.core.model.metadata.editor.MetadataTableEditor;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-3-27 上午11:00:36 (星期五, 29 九月 2006) qzhang $
 * 
 */
public class MetadataExportXmlCommandExt extends Command {

    private File file;

    private MetadataTableEditor extendedTableModel;

    /**
     * amaumont MetadataPasteCommand constructor comment.
     * 
     * @param extendedTableModel
     * @param extendedTable
     * @param validAssignableType
     * @param indexStartAdd
     */
    public MetadataExportXmlCommandExt(MetadataTableEditor extendedTableModel, File file) {
        super();
        this.file = file;
        this.extendedTableModel = extendedTableModel;
    }

    /*
     * (non-Java)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#execute()
     */
    @Override
    public void execute() {
        try {
            if (file != null) {
                file.createNewFile();
                if (extendedTableModel != null) {
                    IMetadataTable currentTable = extendedTableModel.getMetadataTable();
                    // get all the columns from the table
                    if (currentTable != null) {
                        MetadataSchemaExt ext = new MetadataSchemaExt();
                        ext.saveColumnsToFile(file, currentTable);
                    }
                }
            }
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (ParserConfigurationException e) {
            ExceptionHandler.process(e);
        }

    }

}
