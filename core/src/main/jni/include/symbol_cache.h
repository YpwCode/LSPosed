//
// Created by kotori on 2/7/21.
//

#ifndef LSPOSED_SYMBOL_CACHE_H
#define LSPOSED_SYMBOL_CACHE_H

#include <memory>

namespace SandHook {
    class ElfImg;
}

namespace lspd {
    std::unique_ptr<const SandHook::ElfImg> &GetArt(bool release=false);
}

#endif //LSPOSED_SYMBOL_CACHE_H
