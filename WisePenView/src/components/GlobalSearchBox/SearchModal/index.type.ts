export interface SearchModalProps {
  /** Modal 受控开关，由父级 GlobalSearchBox 持有 */
  open: boolean;
  /** Esc / 遮罩点击 / 命中点击成功 都会通过这个回调通知父级关闭 */
  onCancel: () => void;
}
