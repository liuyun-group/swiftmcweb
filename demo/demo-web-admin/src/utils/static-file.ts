
const PREFIX = window.location.origin + "/api/";

/**
 * 获取静态文件 url
 *
 * @param file_pretty_key 静态文件 pretty key
 */
export function gen_static_file_url(file_pretty_key: string): string {
    return PREFIX + file_pretty_key;
}

export function dec_static_file_url(static_file_url: string): string {
    return static_file_url.substring(PREFIX.length);
}
