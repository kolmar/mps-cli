/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package org.mps_cli.gradle.plugin

class ModelDependenciesBuildingTest extends TestBase {

    def "model dependencies extraction test"() {
        given:
        projectName = proj
        settingsFile << ""
        buildFile << "${buildPreamble4ModelDependenciesExtraction()}" + '''

task extractModelDependencies {
    dependsOn buildModelDependencies
    doLast {
        def model2AllDownstreamDependencies = buildModelDependencies.model2AllDownstreamDependencies
        def model2AllUpstreamDependencies = buildModelDependencies.model2AllUpstreamDependencies
        
        for (Object model : model2AllDownstreamDependencies.keySet()) {
            println "downstream dependencies for ${model.name} are: ${model2AllDownstreamDependencies[model].collect { it.name }.sort()}"
        }

        for (Object model : model2AllUpstreamDependencies.keySet()) {
            println "upstream dependencies for ${model.name} are: ${model2AllUpstreamDependencies[model].collect { it.name }.sort()}"
        }
    }
}

'''

        when:
        runTask("extractModelDependencies")

        then:
        // check downstream dependencies
        result.output.contains("downstream dependencies for mps.cli.lanuse.library_second.library_top are: []") ||
                result.output.contains("downstream dependencies for mps.cli.lanuse.library_second.default_persistency.library_top are: []")
        result.output.contains("downstream dependencies for mps.cli.lanuse.library_top.authors_top are: [mps.cli.lanuse.library_second.library_top, mps.cli.lanuse.library_top.library_top]") ||
                result.output.contains("downstream dependencies for mps.cli.lanuse.library_top.default_persistency.authors_top are: [mps.cli.lanuse.library_second.default_persistency.library_top, mps.cli.lanuse.library_top.default_persistency.library_top]")

        // check upstream dependencies
        result.output.contains("upstream dependencies for mps.cli.lanuse.library_second.library_top are: [mps.cli.lanuse.library_top.authors_top]") ||
                result.output.contains("upstream dependencies for mps.cli.lanuse.library_second.default_persistency.library_top are: [mps.cli.lanuse.library_top.default_persistency.authors_top]")
        result.output.contains("upstream dependencies for mps.cli.lanuse.library_top.authors_top are: []") ||
                result.output.contains("upstream dependencies for mps.cli.lanuse.library_top.default_persistency.authors_top are: []")

        where:
        proj                                 | library_top_dot_library_top                                  | library_top_dot_authors_top                                  | library_second_dot_library_top
        "mps_cli_lanuse_file_per_root"       | "mps.cli.lanuse.library_top.library_top"                     | "mps.cli.lanuse.library_top.authors_top"                     | "mps.cli.lanuse.library_second.library_top"
        "mps_cli_lanuse_default_persistency" | "mps.cli.lanuse.library_top.default_persistency.library_top" | "mps.cli.lanuse.library_top.default_persistency.authors_top" | "mps.cli.lanuse.library_second.default_persistency.library_top"
        "mps_cli_lanuse_binary"              | "mps.cli.lanuse.library_top.library_top"                     | "mps.cli.lanuse.library_top.authors_top"                     | "mps.cli.lanuse.library_second.library_top"
    }

    def buildPreamble4ModelDependenciesExtraction()
    {
        """ 
plugins {
    id('org.mps_cli.gradle.plugin')
}

buildModelDependencies {
   sourcesDir = ['../../../../../../../../../mps_test_projects/$projectName']   
}"""
    }
}