// DESCRIPTION: Verilator: Verilog example module
//
// This file ONLY is placed under the Creative Commons Public Domain, for
// any use, without warranty, 2017 by Wilson Snyder.
// SPDX-License-Identifier: CC0-1.0
//======================================================================

// Include common routines
#include <verilated.h>

// Include model header, generated from Verilating "top.v"
#include "Vtop.h"
#include <iostream>
#include <fstream>

#define PMEM_SIZE 1024

// 假设 pmem 是一个简单的数组
uint8_t pmem[PMEM_SIZE];

// 读取 pmem 的函数
uint8_t pmem_read(uint32_t addr) {
    if (addr < PMEM_SIZE) {
        return pmem[addr];
    } else {
        std::cerr << "Read address out of bounds: " << addr << std::endl;
        return 0;
    }
}

// 写入 pmem 的函数
void pmem_write(uint32_t addr, uint8_t data) {
    if (addr < PMEM_SIZE) {
        pmem[addr] = data;
    } else {
        std::cerr << "Write address out of bounds: " << addr << std::endl;
    }
}

static Vtop* top;

static void sim_start(){
    top->clk = 0;
    top->rst = 1;
    top->eval();
    top->rst = 0;
    top->eval();
}

static void sim_end(){
    top->clk = 1;
    top->eval();
    top->clk = 0;
    top->eval();
}

int main(int argc, char** argv) {
    // See a similar example walkthrough in the verilator manpage.

    // This is intended to be a minimal example.  Before copying this to start a
    // real project, it is better to start with a more complete example,
    // e.g. examples/c_tracing.

    // Construct a VerilatedContext to hold simulation time, etc.
    VerilatedContext* const contextp = new VerilatedContext;

    // Pass arguments so Verilated code can see them, e.g. $value$plusargs
    // This needs to be called before you create any model
    contextp->commandArgs(argc, argv);

    // Construct the Verilated model, from Vtop.h generated from Verilating "top.v"
    top = new Vtop{contextp};

    // 初始化 pmem
    std::fill(std::begin(pmem), std::end(pmem), 0);

    // 电路初始化
    sim_start();

    // Simulate until $finish
    while (!contextp->gotFinish()) {
        // 在这里添加对 pmem 的访问和操作
        // 例如，假设 top 有一个地址和数据总线
        uint32_t addr = top->addr;
        uint8_t data = top->data;
        bool write_enable = top->we;

        if (write_enable) {
            pmem_write(addr, data);
        } else {
            top->data = pmem_read(addr);
        }

        // Evaluate model
        top->eval();

        // 时钟跳变
        sim_end();
    }

    // Final model cleanup
    top->final();

    // Destroy model
    delete top;
    delete contextp;

    // Return good completion status
    return 0;
}