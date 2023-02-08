import {
  MenuItem,
  Pagination,
  Select,
  Stack,
  TableCell,
  TableFooter,
  TableRow,
} from '@mui/material';
import {useSelector} from 'react-redux';
import {selectCertificatesPage} from '../../../store/certificates/certificateSelectors';

const CertificateTablePagination = ({changePage, changeSize}) => {
  const {number, size, totalPages} = useSelector(selectCertificatesPage);

  const handleChangePage = (_, newPage) => {
    changePage(newPage);
  };

  const handleChangeRowsPerPage = event => {
    changeSize(event.target.value);
  };

  return (
    <TableFooter>
      <TableRow>
        <TableCell colSpan={7}>
          <Stack direction="row" alignItems="center">
            <Pagination
              sx={{marginLeft: 'auto'}}
              count={totalPages}
              color="primary"
              page={number + 1}
              siblingCount={2}
              onChange={handleChangePage}
            />
            <Select
              sx={{marginLeft: 'auto'}}
              size="small"
              defaultValue={20}
              value={size}
              onChange={handleChangeRowsPerPage}>
              <MenuItem value={10}>10</MenuItem>
              <MenuItem value={20}>20</MenuItem>
              <MenuItem value={50}>50</MenuItem>
            </Select>
          </Stack>
        </TableCell>
      </TableRow>
    </TableFooter>
  );
};

export default CertificateTablePagination;
