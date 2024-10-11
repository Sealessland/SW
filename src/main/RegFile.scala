package raypc
import chisel3._
import chisel3.util._

class RegFileIO(xlen: Int) extends Bundle
{
    val raddr1 = Input(UInt(5.W))
    val raddr2 = Input(UInt(5.W))
    val rdata1 = Output(UInt(xlen.W))
    val rdata2 = Output(UInt(xlen.W))
    val wen = Input(Bool())
    val Waddr = Input(UInt(5.W))
    val Wdata = Input(UInt(xlen.W))
} 

class RegFile (xlen: Int) extends Module {
    val io = IO(new RegFileIO(xlen))
    val regs = Mem(32, UInt(xlen.W)) // Modified line
    io.rdata1 := regs(io.raddr1)
    io.rdata2 := regs(io.raddr2)
    when(io.wen && io.Waddr =/= 0.U){
        regs(io.Waddr) := io.Wdata
    }
    
}