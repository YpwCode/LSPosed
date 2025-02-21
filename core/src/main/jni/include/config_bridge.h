#pragma once

#include <map>

namespace lspd {
    using obfuscation_map_t = std::map<std::string, std::string>;

    class ConfigBridge {
    public:
        inline static ConfigBridge *GetInstance() {
            return instance_.get();
        }

        inline static std::unique_ptr<ConfigBridge> ReleaseInstance() {
            return std::move(instance_);
        }

        virtual obfuscation_map_t &obfuscation_map() = 0;

        virtual void obfuscation_map(obfuscation_map_t) = 0;

        virtual ~ConfigBridge() = default;

    protected:
        static std::unique_ptr<ConfigBridge> instance_;
    };
}
