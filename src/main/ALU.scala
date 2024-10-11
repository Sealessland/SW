package raypc

import chisel3._
import chisel3.util._

object Alu_op {
  val ALU_add = 0.U
}

class ALUIO extends Bundle {
  val A = Input(UInt(32.W))
  val B = Input(UInt(32.W))
  val op = Input(UInt(1.W))
  val out = Output(UInt(32.W))
}

class ALU extends Module {
  val io = IO(new ALUIO)
  val out = Mux(io.op === 1.U, io.A + io.B, 0.U)
  io.out := out
}
