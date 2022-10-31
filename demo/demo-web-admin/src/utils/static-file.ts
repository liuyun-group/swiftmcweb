/**
 * 获取静态文件 url
 *
 * @param file_pretty_key 静态文件 pretty key
 */
export function gen_static_file_url(file_pretty_key: string | null | undefined): string | null {
    return file_pretty_key ? window.location.origin + "/api/" + file_pretty_key : null;
}
