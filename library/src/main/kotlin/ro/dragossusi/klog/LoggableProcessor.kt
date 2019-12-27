package ro.dragossusi.klog

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import ro.dragossusi.klog.annotations.Loggable
import ro.dragossusi.klog.logger.Logger
import ro.dragossusi.klog.writer.LogWriter
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class LoggableProcessor : AbstractProcessor() {

    private val annotation = Loggable::class.java

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Loggable::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val elements = roundEnvironment.getElementsAnnotatedWith(annotation)
        if (elements.isEmpty()) return true
        val kaptGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        if (kaptGeneratedDir.isNullOrEmpty()) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Can't find the target directory for generated Kotlin files."
            )
            return false
        }
        for (element in elements) {
            if (element.kind != ElementKind.CLASS) {
//                processingEnv.messager.printMessage(
//                    Diagnostic.Kind.ERROR, "@Loggable can't be applied to $element: must be a Kotlin class",
//                    element
//                )
                continue
            }
            generateClass(kaptGeneratedDir, element)
        }
        return true
    }

    fun generateClass(kaptGeneratedDir: String, element: Element) {
        val packageName = processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString()
        val simpleName = (element.getAnnotation(Loggable::class.java).name.ifEmpty {
            element.simpleName.toString()
        })
        val file = File(kaptGeneratedDir).apply {
            mkdir()
        }
        FileSpec.builder(
            packageName,
            simpleName + "Logger"
        )
            .addType(createClass(element, packageName, simpleName))
            .build()
            .writeTo(file)
    }

    fun createClass(element: Element, packageName: String, simpleName: String): TypeSpec {
        val typeElement = element as TypeElement
        val elementClassName = ClassName(packageName, simpleName)
        val superInterface = Logger::class.asClassName()
            .parameterizedBy(elementClassName)
        return TypeSpec.classBuilder(simpleName + "Logger")
            .addSuperinterface(superInterface)
            .addFunction(
                generateLogMethod(typeElement.enclosedElements, elementClassName)
            )
            .build()
    }

    fun generateLogMethod(
        properties: Iterable<Element>,
        elementClassName: ClassName
    ): FunSpec {
        val fields = properties.filter {
            it.getAnnotation(Loggable::class.java) != null && it.kind != ElementKind.CLASS
        }
        val builder = FunSpec.builder("log")
            .addParameter("logWriter", LogWriter::class)
            .addParameter("item", elementClassName)
            .addModifiers(KModifier.OVERRIDE)
        createLogFunction(builder, fields.map {
            val simpleName = it.simpleName.toString().removeSuffix("\$annotations")
            it.getAnnotation(Loggable::class.java).name.ifEmpty {
                simpleName
            } to simpleName
        })
        return builder.build()
    }

    fun createLogFunction(funBuilder: FunSpec.Builder, pairs: Iterable<Pair<String, String>>) {
        funBuilder.addStatement("logWriter.startLog()")
        pairs.forEach {
            funBuilder.addStatement("logWriter.write(\"${it.first}\" to item.${it.second})")
        }
        funBuilder.addStatement("logWriter.endLog()")
    }

}

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"