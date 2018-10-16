import scala.io.Source
import java.io._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.feature.IndexToString

//lee el dataset y lo transforma en una lista.
//val jssFile = Source.fromFile("iris.csv").getLines().toList
//lee el dataset con los nombres de las columnas
val df = spark.read.option("inferSchema","true").csv("iris.csv").toDF("c1","c2","c3","c4","c5")
df.show()
//creamos uns columna llamada "label" que contenga 1, 2 y 3 en lugar de los nombres de los tipos de iris.
//usamos when como condicion para saber si es igual a determinado dato y cambiarlo a un numero.
val df2 = df.withColumn("label", when(col("c5") === "Iris-setosa",1.0)
.otherwise(when(col("c5") === "Iris-versicolor", 2.0)
.otherwise(3.0)))
//Con assembler creamos un vector que contenga las primeras 4 columnas que son las caracteristicas y la muestre en una columna
//llamada "caracteristicas"
val assembler = new VectorAssembler().setInputCols(Array("c1", "c2", "c3", "c4")).setOutputCol("caracteristicas")
// creamos una variable "data" y la transformamos con assembler que contiene el vector de las caracteristicas
val data = assembler.transform(df2)
//Esta parte no la utilizamos pero se puede obtener los label y las caracteristicas en la variable data2
val data2 = data.select("label","caracteristicas")
//
val d1 = data2.select("caracteristicas")


// val pw = new PrintWriter(new File("pruebatexto.txt"))
// for(a <- data){
//   val d = a(0);
//   pw.println("%f".format(d))
//  }
//  pw.close()


//
val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(data)
println(s"Found labels: ${labelIndexer.labels.mkString("[", ", ", "]")}")
//
val featureIndexer = new VectorIndexer().setInputCol("caracteristicas").setOutputCol("indexedFeatures").setMaxCategories(4).fit(data)
//
val splits = data.randomSplit(Array(0.6, 0.4))
val trainingData = splits(0)
val testData = splits(1)

val layers = Array[Int](4, 1, 5, 3)

//
val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setLabelCol("indexedLabel").setFeaturesCol("indexedFeatures").setBlockSize(128).setSeed(System.currentTimeMillis).setMaxIter(200)


//
val labelConverter = new IndexToString().setInputCol("prediction").setOutputCol("predictedLabel").setLabels(labelIndexer.labels)



//
val pipeline = new Pipeline()
.setStages(Array(labelIndexer, featureIndexer, trainer, labelConverter))

val model = pipeline.fit(trainingData)

val predictions = model.transform(testData)
predictions.show(5)


val evaluator = new MulticlassClassificationEvaluator()
.setLabelCol("indexedLabel")
.setPredictionCol("prediction")
.setMetricName("accuracy")
val accuracy = evaluator.evaluate(predictions)
println("Test Error = " + (1.0 - accuracy))
