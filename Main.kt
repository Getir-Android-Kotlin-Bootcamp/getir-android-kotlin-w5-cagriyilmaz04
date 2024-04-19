package org.example



import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalCoroutinesApi
fun main() {
    //Slide 6
    CoroutineScope(IO).launch {
        delay(5000)
        println("Merhaba Getir&Patika")
    }

    //Slide 7

    val test = GlobalScope.async {
        "Çağrı Yılmaz"
    }

    runBlocking {
        val print = test.await()
        println(print)
    }

    //Slide 8
    runBlocking {
        delay(10000)
        println("Merhaba Getir&Patika")
    }

    //Slide 9
    /*
    try {
        CoroutineScope(Main).launch {
            val text = "Getir Hw Last"
            //Alt kısım intellij idea da yazdığımızdan dolayı çalışmaz fakat herhangi bir text view açıp textView id demek yetiyor
            // textView.text = text
        }
    }catch (e:Exception){

    }
    */

    //Slide 10
    /*
    CoroutineScope(Dispatchers.IO).launch {
        val url = "https://example.com/api/data"
        val response = URL(url).readText()
        println(response)
    }
    */

    //Slide 11
    CoroutineScope(Dispatchers.Default).launch {
        var sum = 0
        for(i in 5..300000){
            sum += i
        }
        println("Toplam: $sum")
    }

    //Slide 12
    /*
    val url = "https://example.com/api/data"
    CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.IO){
            val respond = URL(url).readText()
            println(respond)
        }
    }
    */

    //Slide 13

    val scope = CoroutineScope(Dispatchers.IO)
    val job = Job()

    scope.launch {
        print("Çağrı Yılmaz")
    }
    job.cancel()

    //Slide 15
    val num = getNumbers(50)
    for(i in num) {
        println(i)
    }

    //Slide 16
    val flow = getNumbersFlow(200)
    CoroutineScope(Default).launch {
        flow.collect {
            println(it)
        }
    }

    //Slide 17
    val scopeNew = CoroutineScope(IO)
    scopeNew.launch {
        println("Çağrı Here")
    }

    //Slide 18
    val supervisorJob = SupervisorJob()
    val scopeLast = CoroutineScope(supervisorJob)

    scopeLast.launch {
        println("Proccess 1 with supervisor job")
    }
    scopeLast.launch {
        println("Proccess 2 with supervisor job")
    }
    //Slide 19
    val unsupervisorJob = SupervisorJob()
    val lastScope = CoroutineScope(unsupervisorJob)

    lastScope.launch {
        println("Proccess 1 without supervisor job")
    }
    lastScope.launch {
        println("Proccess 2 without supervisor job")
    }
    //Slide 20

    /*

    try {
        val uiScope = MainScope()
        uiScope.launch(Dispatchers.Main) {
            println("Harun Hoca")
        }
    }catch (e:Exception){

    }
    */



    //Slide 23
    val flowDiagram = flow {
        emit("Muhammet")
        emit("Çağrı")
        emit("Yılmaz")

    }
    GlobalScope.launch {
        flowDiagram.collect {value -> println(value) }
    }
    //Slide 24
    val sayilar = listOf(6,7,8,9,10)
    val flowSlide24 = sayilar.asFlow()
    GlobalScope.launch {
        flowSlide24.collect { deger -> println(deger) }
    }


    //Slide 25
    val numbersFlow = flow{
        emit(1)
        emit(2)
        emit(3)
    }

    val squareFlow = numbersFlow.map { num -> num*num }
    GlobalScope.launch {
        squareFlow.collect { square -> println(square) }
    }


    //Slide 26
    val sayilarFlow = flow {
        emit(11)
        emit(24)
        emit(35)
        emit(42)
        emit(57)
    }

    val oddFlow = sayilarFlow.filter { num -> num % 2 == 1 }
    GlobalScope.launch {
        oddFlow.collect {tek -> println(tek) }
    }

    //Slide 28

    val strFlow = flow {
        emit("Cagri")
        emit("Abdullah")
        emit("Onur")
        emit("Emir")
    }
    GlobalScope.launch {
        strFlow.flowOn(IO).collect {name -> println(name) }
    }

    //Slide 31
    cache()

    //Slide 32
    cacheChannel()

    //Slide 33
    channelMutex()



}

//Slide 12

val scope = CoroutineScope(IO)

fun myCoroutine(job:Job) {
    scope.launch {
        withContext(job) {

        }
    }
}

//Slide 15

fun getNumbers(delay: Long): List<Int> {
    val numbers = mutableListOf<Int>()
    for(i in 3..12) {
        Thread.sleep(delay)
        numbers.add(i)
    }
    return numbers
}

//Slide 16
fun getNumbersFlow(delay: Long): Flow<Int> {
    return flow {
        for (i in 2..12) {
            emit(i)
            delay(delay)
        }
    }
}

//Slide 31

fun cache() = runBlocking {
    val chanel = Channel<Int>()
    launch {
        println("Producer Lock Variable")
        chanel.send(10)
        println("Producer Unlock Variable")
    }

    launch {
        println("Consumer Lock Variable")
        val value = chanel.receive()
        println("Producer Unlock Variable: Took the variable $value")
    }

}

fun cacheChannel() = runBlocking {
    val chanel = Channel<String>()
    launch {
        println("Producer Lock Variable")
        for(i in 2..9) {
            chanel.send("Message for value $i")
        }
        println("Producer Unlock Variable")
    }

    launch {
        println("Consumer Lock Variable")
        while (true) {
            val msg = chanel.receive()
            println("Consumer $msg")
        }
    }

}

fun channelMutex() = runBlocking {
    val channel = ticker(2000,0)

    launch {
        println("Receiver started")
        while (true) {
            channel.receive()
            println("Clicked is taken")
        }

    }
    delay(10000)

}
