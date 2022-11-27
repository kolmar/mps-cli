package org.mps_cli.model.builder

import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovy.xml.XmlParser
import org.mps_cli.model.SModel

import static groovy.io.FileType.FILES

class SModelBuilder {

    def build(path) {
        Date start = new Date()

        def pathToModelFile = path + File.separator + ".model"
        def modelXML = new XmlParser().parse(pathToModelFile)
        def sModel = new SModel()
        def ref = modelXML.'@ref'
        sModel.modelId = ref.substring(0, ref.indexOf('('))
        sModel.name = ref.substring(ref.indexOf('(') + 1, ref.indexOf(')'))

        def filePath = new File(path)
        def filterFilePerRoot = ~/.*\.mpsr$/
        filePath.traverse type : FILES, nameFilter : filterFilePerRoot, {
            def builder = new RootNodeFromMpsrBuilder()
            def root = builder.build(it, sModel)
            sModel.rootNodes.add(root)
        }

        Date stop = new Date()
        TimeDuration td = TimeCategory.minus( stop, start )
        println "${td} for handling ${path}"

        sModel
    }


}