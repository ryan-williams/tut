package tut

import java.io.{ File, PrintWriter }

import scala.tools.nsc.interpreter.{ IMain, IR, Results }

sealed trait Interp {
  def reset(): Unit
  def processArguments(args: List[String]): Unit
  def interpret(text: String): IR.Result
}
case class Scalac(imain: IMain) extends Interp {
  override def reset(): Unit = imain.reset()
  override def processArguments(args: List[String]): Unit = {
    imain.settings.processArguments(args, true)
    ()
  }
  override def interpret(text: String): IR.Result = imain.interpret(text)
}

case class Ammonite() extends Interp {
  var repl: Repl = _
  override def reset(): Unit = {
    repl = new Repl
    ()
  }
  override def processArguments(args: List[String]): Unit = {
    // TODO
    ()
  }
  override def interpret(text: String): Results.Result = {
    val t @ (ev, out, res, warn, err, inf) = repl.run(text, 0)

    if (ev.isSuccess)
      Results.Success
    else
      Results.Error
  }
}


final case class TutState(
  isCode: Boolean,
  mods: Set[Modifier],
  needsNL: Boolean,
  imain: Interp,
  pw: PrintWriter,
  spigot: Spigot,
  partial: String,
  err: Boolean,
  in: File,
  opts: List[String]
)
