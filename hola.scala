def isPrime(num:Int): Boolean = {
    for(n <- Range(2, num)){
        if(num%n == 0){
            return false
        }
    }
    return true
}

println(isPrime(10))
println(isPrime(23))
