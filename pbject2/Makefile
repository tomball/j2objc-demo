

J2OBJC_HOME := /Users/leogeng/Documents/java/j2objc-1.0.2
SRC_DIR := ../j2ObjcDemon_SharedLib/src/main


# Find all files in SRC_DIR, but remove SRC_DIR from the paths
SRC_FILES := $(subst $(SRC_DIR)/, , $(shell find "$(SRC_DIR)" -name '*.java' -type f))
SRC_FOLDERS := $(dir $(addprefix $(SRC_DIR)/, $(SRC_FILES)))

ifndef DERIVED_FILES_DIR
	XCODE_PROJECT := $(shell find . -maxdepth 1 -type d -name '*.xcodeproj')
	BUILD_CONFIGURATION := Debug
	SDK := iphonesimulator
	DERIVED_FILES_DIR := $(shell xcodebuild -project "$(XCODE_PROJECT)" -showBuildSettings \
		-configuration $(BUILD_CONFIGURATION) -sdk $(SDK) \
		| grep DERIVED_FILES_DIR \
		| sed -E "s/[^=]+=[[:space:]]+(.+)/\1/")
	J2OBJC_HOME := $(shell xcodebuild -project "$(XCODE_PROJECT)" -showBuildSettings \
		-configuration $(BUILD_CONFIGURATION) -sdk $(SDK) \
		| grep J2OBJC_HOME \
		| sed -E "s/[^=]+=[[:space:]]+(.+)/\1/")
endif

ifeq ($(BUILD_CONFIGURATION), Debug)
	J2OBJC_FLAGS += -g
endif

BUILD_DIR := $(DERIVED_FILES_DIR)
J2OBJC_FLAGS += --no-package-directories -use-arc
# Flatten the list of Objective-C files as that is how Xcode will expect them
OBJC_FILES := $(addprefix $(BUILD_DIR)/, $(notdir $(SRC_FILES:.java=.m)))
vpath %.java $(SRC_FOLDERS)

OBJC_HEADERS := $(OBJC_FILES:.m=.h)

TRANSLATE_LIST=$(BUILD_DIR)/.translate_list

.PHONY: clean init-translate-list

translate: $(BUILD_DIR) init-translate-list $(OBJC_FILES) $(OBJC_HEADERS)
	@if [ -s $(TRANSLATE_LIST) ]; then \
		xargs "$(J2OBJC_HOME)/j2objc" $(J2OBJC_FLAGS) -use-arc --prefix main=DT -g -classpath build/classes/main -d $(BUILD_DIR) < "$(TRANSLATE_LIST)"; \
	fi
	rm -f "$(TRANSLATE_LIST)"

$(BUILD_DIR)/%.m $(BUILD_DIR)/%.h: %.java
	@echo $< >> "$(TRANSLATE_LIST)"

init-translate-list: $(BUILD_DIR)
	@rm -f "$(TRANSLATE_LIST)"
	@touch "$(TRANSLATE_LIST)"

$(BUILD_DIR):
	mkdir -p "$(BUILD_DIR)"

clean:
	rm -rf "$(BUILD_DIR)"

show:
	@echo 'DERIVED_FILES_DIR: ' $(DERIVED_FILES_DIR)
	@echo 'SRC_FILES: ' $(SRC_FILES)
	@echo 'SRC_FOLDERS: ' $(SRC_FOLDERS)
	@echo 'OBJC_FILES: ' $(OBJC_FILES)
	@echo 'OBJC_HEADERS: ' $(OBJC_HEADERS)
	@echo 'J2OBJC_FLAGS: ' $(J2OBJC_FLAGS)
