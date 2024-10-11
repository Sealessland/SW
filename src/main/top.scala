package raypc
import chisel3._

class top extends Module {
  val io = IO(new Bundle {
    // 定义顶层模块的输入输出端口
    val raddr1 = Input(UInt(5.W))
    val raddr2 = Input(UInt(5.W))
    val rdata1 = Output(UInt(32.W))
    val rdata2 = Output(UInt(32.W))
    val wen = Input(Bool())
    val waddr = Input(UInt(5.W))
    val wdata = Input(UInt(32.W))
    val aluA = Input(UInt(32.W))
    val aluB = Input(UInt(32.W))
    val aluOp = Input(UInt(1.W))
    val aluOut1 = Output(UInt(32.W))
    val aluOut2 = Output(UInt(32.W))
  })

  val regs = Module(new RegFile(32))
  val ALU1 = Module(new ALU)
  val ALU2 = Module(new ALU)

  // 连接 RegFile 模块
  regs.io.raddr1 := io.raddr1
  regs.io.raddr2 := io.raddr2
  regs.io.wen := io.wen
  regs.io.Waddr := io.waddr
  regs.io.Wdata := io.wdata
  io.rdata1 := regs.io.rdata1
  io.rdata2 := regs.io.rdata2

  // 连接 ALU1 模块
  ALU1.io.A := io.aluA
  ALU1.io.B := io.aluB
  ALU1.io.op := io.aluOp
  io.aluOut1 := ALU1.io.out

  // 连接 ALU2 模块
  ALU2.io.A := io.aluA
  ALU2.io.B := io.aluB
  ALU2.io.op := io.aluOp
  io.aluOut2 := ALU2.io.out
}
