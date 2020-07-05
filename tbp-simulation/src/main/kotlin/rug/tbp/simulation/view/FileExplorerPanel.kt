package rug.tbp.simulation.view

import java.awt.BorderLayout
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

/**
 * Credits: http://www.java2s.com/Code/Java/Swing-JFC/DisplayafilesysteminaJTreeview.htm
 */
class FileExplorerPanel(private val dir: File) : JPanel() {

    var tree = JTree(addNodes(dir = dir))

    init {
        layout = BorderLayout()
        val scrollPane = JScrollPane()
        scrollPane.viewport.add(tree)
        add(BorderLayout.CENTER, scrollPane)
        tree.dropTarget = object : DropTarget() {
            override fun drop(dtde: DropTargetDropEvent) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                    for (file in droppedFiles) {
                        Files.copy(file.toPath(), dir.toPath().resolve(file.name), StandardCopyOption.REPLACE_EXISTING)
                    }
                    dtde.dropComplete(true)
                    tree.model = DefaultTreeModel(addNodes(dir = dir))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun addNodes(curTop: DefaultMutableTreeNode? = null, dir: File) : DefaultMutableTreeNode{

        val curPath = dir.path
        val curDir = DefaultMutableTreeNode(curPath)

        curTop?.add(curDir)

        val ol = Vector<String>()
        val tmp = dir.list()!!
        for(string in tmp)
            ol.addElement(string)

        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER)
        var f: File
        val files = Vector<String>()
        for(i in 0 until ol.size){
            val thisObject = ol.elementAt(i)
            val newPath = if(curPath == ".")
                thisObject
            else
                curPath + File.separator + thisObject
            f = File(newPath)
            if(f.isDirectory)
                addNodes(curDir, f)
            else if(thisObject.endsWith(".csv"))
                files.addElement(thisObject)
        }
        for (fNum in files.indices)
            curDir.add(DefaultMutableTreeNode(files.elementAt(fNum)))
        return curDir
    }

}