package rug.tbp.simulation.view

import java.awt.BorderLayout
import java.io.File
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Credits: http://www.java2s.com/Code/Java/Swing-JFC/DisplayafilesysteminaJTreeview.htm
 */
class FileExplorerPanel(dir: File) : JPanel() {

    val tree = JTree(addNodes(dir = dir))

    init {
        layout = BorderLayout()
        val scrollPane = JScrollPane()
        scrollPane.viewport.add(tree)
        add(BorderLayout.CENTER, scrollPane)
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
            else
                files.addElement(thisObject)
        }
        for (fNum in files.indices)
            curDir.add(DefaultMutableTreeNode(files.elementAt(fNum)))
        return curDir
    }

}