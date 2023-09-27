//
//  FrameProcessorLoader.m
//  zLogistics
//
//  Created by Mac-de-Eric on 26/09/2023.
//

#import <Foundation/Foundation.h>
#import <VisionCamera/FrameProcessorPlugin.h>
#import <VisionCamera/FrameProcessorPluginRegistry.h>
#import "SyartecVisionFrameProcessor-Swift.h"

@interface FrameProcessorLoader : NSObject
@end
@implementation FrameProcessorLoader

+ (void) load {
  [FrameProcessorPluginRegistry addFrameProcessorPlugin:@"vision_camera_barcode_scanner2"
                                        withInitializer:^FrameProcessorPlugin*(NSDictionary* options) {
    return [[VisionCameraCodeScanner alloc] init];
  }];
  [FrameProcessorPluginRegistry addFrameProcessorPlugin:@"vision_camera_ocr"
                                        withInitializer:^FrameProcessorPlugin*(NSDictionary* options) {
    return [[OCRFrameProcessorPlugin alloc] init];
  }];
}
@end
