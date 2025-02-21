#pragma once

#include "config_bridge.h"
#include "service.h"

namespace lspd {
    class ConfigImpl : public ConfigBridge {
    public:
        inline static void Init() {
            instance_ = std::make_unique<ConfigImpl>();
        }

        virtual obfuscation_map_t &obfuscation_map() override { return obfuscation_map_; }

        virtual void
        obfuscation_map(obfuscation_map_t m) override { obfuscation_map_ = std::move(m); }

    private:
        inline static std::map<std::string, std::string> obfuscation_map_;
    };
}
