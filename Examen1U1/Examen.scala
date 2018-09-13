def esPar(num: Int): Boolean={
  if(num%2 == 0){
    return true
  }
  return false
}

// Buscar numeros pares en una lista de numero enteros
//Cuidado con retornar siempre en las funciones al final
val num = List(10,5,6,4,2)
def listapar(num: List[Int]): String ={
  for (a <- num){
    if(a%2 == 0){
      println(s"$a es par")
    }else{
      println(s"$a no es par")
    }
  }
  return "Done"
}
println(listapar(num))


// afortunado numero 7. El numero 7 en una lista se suma dos veces.
val num2 = List(7,5,6,7)
def siete (num: List[Int]): Int={
  var r = 0
  for (n <- num){
    if(n == 7){
      r = r + n*2
    }else {
      r = r + n
    }
  }
  return r
  }
println(siete(num))

// sumatoria de los lados de una lista
val list = List(1,6,3,4)
def sum(list: List[Int]): Boolean={
  var p = 0
  var s = list.sum
  for(x <- Range(0, list.length)){
    p = p + list(x)
    s = s - list(x)
    if(p == s){
      return true
    }
  }
  return false
}
// otra opcion
def sum2(list: List[Int]): Boolean={
  val (x,y) = list.splitAt(list.length/2)
  if (x.sum == y.sum){ return true }
  else return false
}
//palindromo
def palindromo(p:String): Boolean={
  if(p == p.reverse){
    return true
  }else{
    return false
  }

}
