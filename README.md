# KMorph
A Kotlin library which auto-generate code to copy your data between classes.

## Version
Annotations  [![Download](https://api.bintray.com/packages/gaurangshaha/KMorph/kmorph-annotation/images/download.svg) ](https://bintray.com/gaurangshaha/KMorph/kmorph-annotation/_latestVersion) 

Compiler  [![Download](https://api.bintray.com/packages/gaurangshaha/KMorph-Processor/kmorph-processor/images/download.svg) ](https://bintray.com/gaurangshaha/KMorph-Processor/kmorph-processor/_latestVersion)

## Use case
While implementing [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html), we ended up creating different data classes for each layers.

Lets consider a scenario where you have two objects named as EmployeeDTO and EmployeeDAO. EmployeeDTO belongs to domain layer whereas EmployeeDAO belongs to data layer.

```kotlin
data class EmployeeDTO(var lastName: String, var firstName: String, var idNumber: BigInteger)
```

```kotlin
data class EmployeeDAO(var lastName: String, var firstName: String, var idNumber: BigInteger)
```

EmployeeDAO is the object receiver from employee repository. In order to execute some functionality in interactor we need to pass EmployeeDTO.

To copy attribute value from EmployeeDAO to EmployeeDTO and vice versa, we used to write a code something similar to what shown below.

```java
class EmployeeDAOMorpher {
    fun morph(source: EmployeeDAO): EmployeeDTO {
        val target = EmployeeDTO()
        target.lastName = source.lastName
        target.firstName = source.firstName
        target.idNumber = source.idNumber
        return target
    }

    fun reverseMorph(target: EmployeeDTO): EmployeeDAO {
        val source = EmployeeDAO()
        source.lastName = target.lastName
        source.firstName = target.firstName
        source.idNumber = target.idNumber
        return source
    }
}
```

Let the KMorph library handle this tedious and error-prone task for you. You can achieve above functionality using annotation provided by KMorph library.

## How to use

Add the library dependency to your build.gradle file.

```groovy
dependencies {
    implementation 'com.kmorph:kmorph-annotation:1.0.0'
    annotationProcessor 'com.kmorph:kmorph-processor:1.0.0'
}
```

If you are using any other libraries with AnnotationsProcessors like ButterKnife, Realm, Dagger , etc. You need to set this in your build.gradle to exclude the Processor that is already packaged:

```groovy
packagingOptions {
    exclude 'META-INF/services/javax.annotation.processing.Processor'
}
```

Annotate source class and rebuild the project. KMorph library will generate a morpher class, which will have name as **&lt;yourClassName&gt;Morpher.kt**

In this you will find two extension functions which can be used to copy attributes of object.

If Android Studio is not detecting generated files then you might need to add following lines in app level build.gradle 

```groovy 
android {
	sourceSets {
		debug.java.srcDirs += 'build/generated/source/kaptKotlin/debug'
		release.java.srcDirs += 'build/generated/source/kaptKotlin/release'
	}
}
```

#### @MorphTo(TargetClass::class)

This annotation is used on class which needs be morphed. It expects class of target in which it should be morphed.

In order to generate the morpher, source and target class KMorph library need following thing

* Both the classes should be of type public class i.e. interface, annotation, enum, private and protected class, abstract class can't be used with it.
* Attributes which you need to copy should have getter and setter for them.

KMorph library will auto compare the attributes from both the classes and creates the morpher for you. Morpher will include the attributes which has same name and compatible data types.

If attribute has different names, you can instruct KMorph library to generate mapping using [@MorphToField](#morphtofieldfieldname) annotation. 

If attribute has different data types, you can instruct KMorph library to generate mapping using [@FieldTransformer](#fieldtransformerfieldtransformerclass) annotation.

Note : No annotation will be needed on target class.

#### @MorphToField("fieldName")

This annotation is used on attribute which has different name in both the classes. It expects attribute name from target class with whom it need be morphed.

It can be used with conjunction with [@FieldTransformer](#fieldtransformerfieldtransformerclass) annotation.

Note : This annotation should be used within class which is annotated with [@MorphTo](#morphtotargetclassclass). KMorph library will ignore this annotation if used outside of class annotated by [@MorphTo](#morphtotargetclassclass).

If attributes annotated with this has incompatible data type, KMorph library will ignore it.

#### @FieldTransformer(FieldTransformer.class)

This annotation is used on attribute which has different data type in both the classes. It expects a class which helps library to transform the fields.

Transformer class needs to implement FieldTransformerContract interface provided by KMorph library. Provide implementation for transform and reverseTransform method, library will take care of calling appropriate method.

```kotlin
class MillisToDateStringTransformer : FieldTransformerContract<String, Long> {
    private val DATE_FORMAT = "dd MMM yyyy"

    override fun reverseTransform(target: Long): String {
        return SimpleDateFormat(DATE_FORMAT).format(Date(target))
    }

    override fun transform(source: String): Long {
        return try {
            SimpleDateFormat(DATE_FORMAT).parse(source).time
        } catch (e: ParseException) {
            (-1).toLong()
        }
    }
}
```

It can be used with conjunction with [@MorphToField](#morphtofieldfieldname) annotation.

Note : Either default or public no-arg constructor should be present in transformer class.


## Example
1. [Both are the normal classes](https://github.com/GaurangShaha/KMorph/tree/master/app/src/main/java/com/kmorph/example1)
2. [Both are data classes](https://github.com/GaurangShaha/KMorph/tree/master/app/src/main/java/com/kmorph/example2)
3. [One normal and one data class](https://github.com/GaurangShaha/KMorph/tree/master/app/src/main/java/com/kmorph/example3)
4. [Both are data classes but with different number of constructor parameter](https://github.com/GaurangShaha/KMorph/tree/master/app/src/main/java/com/kmorph/example4)

## License

	Copyright 2018 Gaurang Shaha
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
