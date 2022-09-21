#version 150

vec2 getNormalUV(vec2 UV0, vec4 FullUV) {
	return (UV0 - FullUV.xy) / ((FullUV.z - FullUV.x), (FullUV.w - FullUV.y));
}
