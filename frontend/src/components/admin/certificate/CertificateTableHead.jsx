import {TableCell, TableHead, TableRow} from '@mui/material';
import NoteAddIcon from '@mui/icons-material/NoteAdd';

const HeadCell = ({nowrap, children, ...props}) => (
  <TableCell
    {...props}
    sx={{
      color: 'white',
      backgroundColor: '#757575',
      whiteSpace: nowrap && 'nowrap',
    }}>
    {children}
  </TableCell>
);

const CertificateTableHead = ({handleModalOpen}) => {
  return (
    <TableHead>
      <TableRow>
        <HeadCell>Id</HeadCell>
        <HeadCell>Name</HeadCell>
        <HeadCell>Duration</HeadCell>
        <HeadCell>Price</HeadCell>
        <HeadCell nowrap>Create Date</HeadCell>
        <HeadCell nowrap>Last Update Date</HeadCell>
        <HeadCell align="center">
          <NoteAddIcon onClick={() => handleModalOpen()} />
        </HeadCell>
      </TableRow>
    </TableHead>
  );
};

export default CertificateTableHead;
