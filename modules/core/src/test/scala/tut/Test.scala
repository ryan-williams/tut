package tut

import java.io.{ ByteArrayOutputStream, PrintStream }

import ammonite.MainRunner
import ammonite.main.Cli
import ammonite.util.Colors
import org.scalatest.FunSuite

class Test
  extends FunSuite {

  test("repl") {
    val repl = new Repl

    {
      val t @ (ev, out, res, warn, err, inf) =
        repl.run(
          """import $ivy.`org.typelevel:cats-core_2.12:1.0.1`, cats.syntax.show._
            |import $ivy.`org.hammerlab.math:format_2.12:1.0.0`, hammerlab.math.sigfigs._
            |implicit val sf: SigFigs = 2
            |show((1.111).show)
            |"""
            .stripMargin,
          2
        )
      assert(ev.isSuccess)
      assert(out == "\"1.1\"\n")
      assert(err == "")
    }
  }

  test("main") {
    val out = new ByteArrayOutputStream()
    val err = new ByteArrayOutputStream()

    val m = ammonite.Main(
      outputStream = out,
      errorStream = err,
      colors = Colors.BlackWhite
    )
    m.runCode("println(1 + 1)")
    println("yay")

    out.close()
    err.close()

    val stdout = new String(out.toByteArray)
    val stderr = new String(err.toByteArray)

    println(s"out:\n$stdout\n")
    println(s"err:\n$stderr\n")
  }

  test("runner") {
    val out = new ByteArrayOutputStream()
    val err = new ByteArrayOutputStream()

    val opb = new ByteArrayOutputStream()
    val epb = new ByteArrayOutputStream()

    val op = new PrintStream(opb)
    val ep = new PrintStream(opb)

    val m =
      new MainRunner(
        Cli.Config(
          colored = Some(false)
        ),
        op,
        ep,
        System.in,
        out,
        err
      )

    m.watchLoop(
      isRepl = false,
      printing = true,
      _.runCode("println(1 + 1)")
    )

    out.close()
    err.close()
    op.close()
    ep.close()

    val stdout = new String(out.toByteArray)
    val stderr = new String(err.toByteArray)

    val pout = new String(opb.toByteArray)
    val perr = new String(epb.toByteArray)

    println(s"out:\n$stdout\n")
    println(s"err:\n$stderr\n")
    println(s"pout:\n$pout\n")
    println(s"perr:\n$perr\n")
  }
}
