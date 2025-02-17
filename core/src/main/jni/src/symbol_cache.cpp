

//
// Created by kotori on 2/7/21.
//

#include "symbol_cache.h"
#include "elf_util.h"
#include <dobby.h>
#include "macros.h"
#include "config.h"
#include <vector>
#include <logging.h>

namespace lspd {
    std::unique_ptr<const SandHook::ElfImg> &GetArt(bool release) {
        static std::unique_ptr<const SandHook::ElfImg> kArtImg = nullptr;
        if (release) {
            kArtImg.reset();
        } else if (!kArtImg) {
            kArtImg = std::make_unique<SandHook::ElfImg>(kLibArtName);
        }
        return kArtImg;
    }
}  // namespace lspd
