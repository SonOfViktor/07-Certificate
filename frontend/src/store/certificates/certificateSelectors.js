export const selectCertificatesInfo = state => ({
  certificateList: state.certificates.certificateList,
  status: state.certificates.status,
  error: state.certificates.error,
});

export const selectCertificatesPage = state => {
  return state.certificates.page;
};
